package com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler;

import com.alligator.market.backend.source.adapter.shared.poll.SourcePollHttpException;
import com.alligator.market.backend.source.adapter.shared.poll.SourcePollSkipLogger;
import com.alligator.market.backend.source.adapter.twelvedata.TwelveDataSource;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.support.TwelveDataFxSpotSupportCatalog;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class TwelveDataFxSpotHandler extends AbstractInstrumentHandler<TwelveDataSource, FxSpot> {
    private static final HandlerCode HANDLER_CODE = HandlerCode.of("TWELVE_DATA_FX_SPOT_HANDLER");
    private static final String SOURCE_STREAM_NAME = "TWELVE DATA FX_SPOT";
    private static final TwelveDataFxSpotHandlerPassport PASSPORT = TwelveDataFxSpotHandlerPassport.INSTANCE;

    private static final Set<FxSpot> SUPPORTED_INSTRUMENTS =
            TwelveDataFxSpotSupportCatalog.SUPPORTED_INSTRUMENTS;

    private final WebClient webClient;
    private final String apiKey;
    private final TwelveDataFxSpotHandlerPolicy policy;

    public TwelveDataFxSpotHandler(
            WebClient webClient,
            String apiKey,
            TwelveDataFxSpotHandlerPolicy policy
    ) {
        super(HANDLER_CODE, PASSPORT, FxSpot.class, SUPPORTED_INSTRUMENTS, policy);

        this.webClient = Objects.requireNonNull(webClient, "webClient must not be null");
        this.apiKey = requireNotBlank(apiKey, "apiKey");
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
        SourceInstrumentCode sourceInstrumentCode =
                TwelveDataFxSpotSupportCatalog.sourceInstrumentCodeOf(domainCode);
        String twelveSymbol = TwelveDataFxSpotSupportCatalog.twelveSymbolOf(domainCode);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exchange_rate")
                        .queryParam("symbol", twelveSymbol)
                        .queryParam("apikey", apiKey)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).defaultIfEmpty("").map(body ->
                                new SourcePollHttpException(
                                        response.statusCode(),
                                        body,
                                        "Twelve Data FX_SPOT symbol=" + twelveSymbol
                                )
                        )
                )
                .bodyToMono(JsonNode.class)
                .doOnSubscribe(sub -> log.debug(
                        "Polling FX_SPOT source tick from Twelve Data: instrumentCode={}, symbol={}",
                        domainCode.value(), twelveSymbol))
                .map(body -> {
                    SourceTick tick = mapExchangeRateToSourceTick(sourceInstrumentCode, twelveSymbol, body);
                    log.debug("Received FX_SPOT SourceTick from Twelve Data: {}", tick);
                    return tick;
                });
    }

    private SourceTick mapExchangeRateToSourceTick(
            SourceInstrumentCode sourceInstrumentCode,
            String twelveSymbol,
            JsonNode root
    ) {
        if (root == null || !root.isObject()) {
            throw new IllegalStateException("Twelve Data response must be a JSON object");
        }

        JsonNode statusNode = root.path("status");
        if (statusNode.isString() && "error".equalsIgnoreCase(statusNode.stringValue())) {
            throw new IllegalStateException(
                    "Twelve Data response status=error for symbol " + twelveSymbol + ": " +
                            responseMessage(root)
            );
        }

        BigDecimal rate = decimalValue(root.path("rate"), "rate");
        Instant sourceTickTime = Instant.ofEpochSecond(longValue(root.path("timestamp"), "timestamp"));

        return new SourceLastPriceTick(
                sourceInstrumentCode,
                rate,
                sourceTickTime
        );
    }

    private static String responseMessage(JsonNode root) {
        JsonNode messageNode = root.path("message");
        if (messageNode.isString()) {
            return messageNode.stringValue();
        }
        return "message is missing";
    }

    private static BigDecimal decimalValue(JsonNode node, String fieldName) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            throw new IllegalStateException("Twelve Data field '" + fieldName + "' is missing");
        }

        if (node.isNumber()) {
            return node.decimalValue();
        }

        if (node.isString()) {
            try {
                return new BigDecimal(node.stringValue());
            } catch (NumberFormatException ex) {
                throw new IllegalStateException(
                        "Twelve Data field '" + fieldName + "' must contain a decimal value",
                        ex
                );
            }
        }

        throw new IllegalStateException("Twelve Data field '" + fieldName + "' must be a number or string");
    }

    private static long longValue(JsonNode node, String fieldName) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            throw new IllegalStateException("Twelve Data field '" + fieldName + "' is missing");
        }

        long value;
        if (node.isNumber()) {
            value = node.longValue();
        } else if (node.isString()) {
            try {
                value = Long.parseLong(node.stringValue());
            } catch (NumberFormatException ex) {
                throw new IllegalStateException(
                        "Twelve Data field '" + fieldName + "' must contain an integer value",
                        ex
                );
            }
        } else {
            throw new IllegalStateException("Twelve Data field '" + fieldName + "' must be a number or string");
        }

        if (value <= 0) {
            throw new IllegalStateException("Twelve Data field '" + fieldName + "' must be positive");
        }

        return value;
    }

    private static String requireNotBlank(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
