package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.support.MoexIssFxSpotSupportCatalog;
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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class MoexIssFxSpotHandler extends AbstractInstrumentHandler<MoexIssSource, FxSpot> {
    private static final HandlerCode HANDLER_CODE = HandlerCode.of("MOEX_ISS_FX_SPOT_HANDLER");
    private static final MoexIssFxSpotHandlerPassport PASSPORT = MoexIssFxSpotHandlerPassport.INSTANCE;

    private static final Set<FxSpot> SUPPORTED_INSTRUMENTS = MoexIssFxSpotSupportCatalog.SUPPORTED_INSTRUMENTS;
    private static final Duration POLL_INTERVAL = Duration.ofSeconds(1);
    private static final MoexIssFxSpotHandlerPolicy POLICY = new MoexIssFxSpotHandlerPolicy(POLL_INTERVAL);

    private final WebClient webClient;

    private static final DateTimeFormatter MOEX_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // MOEX SYSTIME is reported without an offset, so parse it as Moscow exchange time.
    private static final ZoneId MOEX_ZONE = ZoneId.of("Europe/Moscow");

    public MoexIssFxSpotHandler(WebClient webClient) {
        super(HANDLER_CODE, PASSPORT, FxSpot.class, SUPPORTED_INSTRUMENTS, POLICY);

        Objects.requireNonNull(webClient, "webClient must not be null");
        this.webClient = webClient;
    }

    @Override
    protected Publisher<SourceTick> doStreamSourceTicks(FxSpot instrument) {
        return streamSourceTicksByPolling(instrument);
    }

    private Publisher<SourceTick> streamSourceTicksByPolling(FxSpot instrument) {
        return pollSourceTickOnce(instrument)
                .onErrorResume(ex -> {
                    log.warn(
                            "Failed to poll FX_SPOT source tick from MOEX ISS: instrumentCode={}, reason={}",
                            instrument.instrumentCode().value(),
                            ex.getMessage(),
                            ex
                    );
                    return Mono.empty();
                })
                .repeatWhen(completed -> completed.delayElements(POLICY.pollInterval()));
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
                        .queryParam("marketdata.columns", "SYSTIME,LAST")
                        .build(secid.value())
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).map(body ->
                                new IllegalStateException("MOEX ISS HTTP error " + response.statusCode()
                                        + " for secid=" + secid.value() + ", body=" + body)
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
        // MOEX ISS returns table-shaped JSON: column names define indexes into each data row.
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
        int lastIdx = indexOfColumn(columns, "LAST");
        if (systimeIdx < 0 || lastIdx < 0) {
            throw new IllegalStateException("Array 'columns' must contain values 'SYSTIME' and 'LAST'");
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
        if (row.size() <= Math.max(systimeIdx, lastIdx)) {
            throw new IllegalStateException(
                    "Marketdata row has insufficient columns for SYSTIME/LAST"
            );
        }

        JsonNode systimeNode = row.get(systimeIdx);
        JsonNode lastNode = row.get(lastIdx);

        if (systimeNode == null || systimeNode.isNull() || !systimeNode.isString()) {
            throw new IllegalStateException("MOEX ISS SYSTIME must be non-null string");
        }
        if (lastNode == null || lastNode.isNull() || !lastNode.isNumber()) {
            throw new IllegalStateException("MOEX ISS LAST must be non-null number");
        }

        String systimeStr = systimeNode.stringValue();

        Instant sourceTimestamp;
        try {
            LocalDateTime ldt = LocalDateTime.parse(systimeStr, MOEX_DATETIME);
            sourceTimestamp = ldt.atZone(MOEX_ZONE).toInstant();
        } catch (DateTimeParseException ex) {
            throw new IllegalStateException("Failed to parse MOEX SYSTIME: '" + systimeStr + "'", ex);
        }

        BigDecimal last = lastNode.decimalValue();

        return new SourceLastPriceTick(
                secid,
                last,
                sourceTimestamp
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
