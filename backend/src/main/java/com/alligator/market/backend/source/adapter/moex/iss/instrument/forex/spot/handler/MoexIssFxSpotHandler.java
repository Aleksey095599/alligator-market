package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.support.MoexIssFxSpotSupportCatalog;
import com.alligator.market.backend.source.adapter.shared.poll.SourcePollHttpException;
import com.alligator.market.backend.source.adapter.shared.poll.SourcePollSkipLogger;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.source.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.source.vo.HandlerCode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class MoexIssFxSpotHandler extends AbstractInstrumentHandler<MoexIssSource, FxSpot> {
    private static final HandlerCode HANDLER_CODE = HandlerCode.of("MOEX_ISS_FX_SPOT_HANDLER");
    private static final String SOURCE_STREAM_NAME = "MOEX ISS FX_SPOT";
    private static final MoexIssFxSpotHandlerPassport PASSPORT = MoexIssFxSpotHandlerPassport.INSTANCE;

    private static final Set<FxSpot> SUPPORTED_INSTRUMENTS = MoexIssFxSpotSupportCatalog.SUPPORTED_INSTRUMENTS;

    private final WebClient webClient;
    private final MoexIssFxSpotHandlerPolicy policy;

    private static final DateTimeFormatter MOEX_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter MOEX_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final ZoneId MOEX_ZONE = ZoneId.of("Europe/Moscow");

    public MoexIssFxSpotHandler(WebClient webClient, MoexIssFxSpotHandlerPolicy policy) {
        super(HANDLER_CODE, PASSPORT, FxSpot.class, SUPPORTED_INSTRUMENTS, policy);

        Objects.requireNonNull(webClient, "webClient must not be null");
        this.webClient = webClient;
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    @Override
    protected Publisher<SourceTick> doStreamSourceTicks(FxSpot instrument) {
        return streamSourceTicksByPolling(instrument);
    }

    private Publisher<SourceTick> streamSourceTicksByPolling(FxSpot instrument) {
        return pollSourceTickOnce(instrument)
                .onErrorResume(ex -> {
                    SourcePollSkipLogger.logSkippedPoll(
                            SOURCE_STREAM_NAME,
                            source().code(),
                            HANDLER_CODE,
                            instrument.instrumentCode(),
                            ex
                    );
                    return Mono.empty();
                })
                .repeatWhen(completed -> completed.delayElements(policy.pollInterval()));
    }

    private Mono<SourceTick> pollSourceTickOnce(FxSpot instrument) {
        InstrumentCode domainCode = instrument.instrumentCode();
        SourceInstrumentCode secid = MoexIssFxSpotSupportCatalog.moexSecidOf(domainCode);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/engines/currency/markets/selt/boards/CETS/securities/{secid}.json")
                        .queryParam("iss.meta", "off")
                        .queryParam("iss.only", "marketdata")
                        .queryParam("marketdata.columns", "SYSTIME,TIME,LAST")
                        .build(secid.value())
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).defaultIfEmpty("").map(body ->
                                new SourcePollHttpException(
                                        response.statusCode(),
                                        body,
                                        "MOEX ISS FX_SPOT secid=" + secid.value()
                                )
                        )
                )
                .bodyToMono(JsonNode.class)
                .doOnSubscribe(sub -> log.debug(
                        "Polling FX_SPOT source tick from MOEX ISS: instrumentCode={}, secid={}",
                        domainCode.value(), secid.value()))
                .map(body -> {
                    SourceTick tick = mapMarketdataToSourceTick(secid, body);
                    log.debug("Received FX_SPOT SourceMarketDataTick from MOEX ISS: {}", tick);
                    return tick;
                });
    }

    private SourceTick mapMarketdataToSourceTick(SourceInstrumentCode secid, JsonNode root) {
        JsonNode marketdata = root.path("marketdata");
        if (marketdata.isMissingNode() || !marketdata.isObject()) {
            throw new IllegalStateException("MOEX ISS response has no 'marketdata' object");
        }

        JsonNode columnsNode = marketdata.path("columns");
        JsonNode dataNode = marketdata.path("data");
        if (!columnsNode.isArray() || !dataNode.isArray()) {
            throw new IllegalStateException("Object 'marketdata' must contain 'columns' and 'data' arrays");
        }

        ArrayNode columns = (ArrayNode) columnsNode;
        ArrayNode data = (ArrayNode) dataNode;

        int systimeIdx = indexOfColumn(columns, "SYSTIME");
        int timeIdx = indexOfColumn(columns, "TIME");
        int lastIdx = indexOfColumn(columns, "LAST");
        if (systimeIdx < 0 || timeIdx < 0 || lastIdx < 0) {
            throw new IllegalStateException("Array 'columns' must contain values 'SYSTIME', 'TIME' and 'LAST'");
        }

        if (data.size() != 1) {
            throw new IllegalStateException(
                    "Array 'data' must contain exactly one row for source instrument " + secid.value() +
                            ", but was: " + data.size()
            );
        }

        JsonNode row = data.get(0);
        if (!row.isArray()) {
            throw new IllegalStateException("Element 0 of 'data' must be an array (marketdata row)");
        }
        if (row.size() <= Math.max(Math.max(systimeIdx, timeIdx), lastIdx)) {
            throw new IllegalStateException(
                    "Marketdata row has insufficient columns for SYSTIME/TIME/LAST"
            );
        }

        JsonNode systimeNode = row.get(systimeIdx);
        JsonNode timeNode = row.get(timeIdx);
        JsonNode lastNode = row.get(lastIdx);

        if (systimeNode == null || systimeNode.isNull() || !systimeNode.isString()) {
            throw new IllegalStateException("MOEX ISS SYSTIME must be non-null string");
        }
        if (timeNode == null || timeNode.isNull() || !timeNode.isString()) {
            throw new IllegalStateException("MOEX ISS TIME must be non-null string");
        }
        if (lastNode == null || lastNode.isNull() || !lastNode.isNumber()) {
            throw new IllegalStateException("MOEX ISS LAST must be non-null number");
        }

        String systimeStr = systimeNode.stringValue();
        String timeStr = timeNode.stringValue();

        Instant sourceTickTime;
        try {
            LocalDate sourceDate = LocalDateTime.parse(systimeStr, MOEX_DATETIME).toLocalDate();
            LocalTime tickTime = LocalTime.parse(timeStr, MOEX_TIME);
            sourceTickTime = LocalDateTime.of(sourceDate, tickTime).atZone(MOEX_ZONE).toInstant();
        } catch (DateTimeParseException ex) {
            throw new IllegalStateException(
                    "Failed to parse MOEX tick time: SYSTIME='" + systimeStr + "', TIME='" + timeStr + "'",
                    ex
            );
        }

        BigDecimal last = lastNode.decimalValue();

        return new SourceLastPriceTick(
                secid,
                last,
                sourceTickTime
        );
    }

    private static int indexOfColumn(ArrayNode columns, String name) {
        for (int i = 0; i < columns.size(); i++) {
            JsonNode columnNode = columns.get(i);

            if (!columnNode.isString()) {
                continue;
            }

            String columnName = columnNode.stringValue();
            if (name.equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }
}
