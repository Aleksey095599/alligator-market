package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.twelve.free.TwelveFreeAdapterV2;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import static com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot.TwelveFreeFxSpotCatalog.SUPPORTED;

/**
 * Обработчик инструментов FX_SPOT для провайдера TwelveData (free).
 */
public class TwelveFreeFxSpotHandler extends AbstractInstrumentHandler<TwelveFreeAdapterV2, FxSpot> {

    /* Уникальный код обработчика. */
    private static final String HANDLER_CODE = "TWELVE_FREE_FX_SPOT_HANDLER";

    private final WebClient webClient;
    private final TwelveFreeConnectionProps props;

    /** Конструктор. */
    public TwelveFreeFxSpotHandler(
            WebClient webClient,
            TwelveFreeConnectionProps props
    ) {
        super(HANDLER_CODE, FxSpot.class, SUPPORTED);
        this.webClient = Objects.requireNonNull(webClient, "webClient must not be null");
        this.props = Objects.requireNonNull(props, "props must not be null");
    }

    /** Реализация получения котировки. */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Провайдер ожидает формат символа инструмента: BASE/QUOTE
        String symbol = instrument.base().code() + "/" + instrument.quote().code();
        return webClient.get()
                .uri(b -> b
                        .path("/price")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> responseJsonToQuoteTick(json, instrument.code()))
                .flux();
    }

    /** Преобразование JSON ответа провайдера в доменную модель котировки. */
    private QuoteTick responseJsonToQuoteTick(JsonNode json, String instrumentCode) {
        JsonNode priceNode = json.get("price");
        if (priceNode == null) {
            throw new IllegalArgumentException("Invalid provider response: " + json);
        }
        BigDecimal price = new BigDecimal(priceNode.asText());
        return new QuoteTick(
                instrumentCode,
                price,
                price,
                Instant.now(),
                provider().providerCode()
        );
    }
}
