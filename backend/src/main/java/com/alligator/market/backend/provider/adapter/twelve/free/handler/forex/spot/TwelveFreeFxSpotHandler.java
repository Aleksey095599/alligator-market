package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.twelve.free.TwelveFreeAdapterV2;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Обработчик инструментов FX_SPOT для провайдера TwelveData (free).
 */
public class TwelveFreeFxSpotHandler extends AbstractInstrumentHandler<TwelveFreeAdapterV2, FxSpot> {

    /* Уникальный код обработчика: UPPERCASE, формат [A-Z0-9_]+. */
    private static final String HANDLER_CODE = "TWELVE_FREE_FX_SPOT_HANDLER";

    /* Поддерживаемые коды инструментов FX_SPOT. */
    private static final Set<String> SUPPORTED_CODES = TwelveFreeFxSpotCatalog.SUPPORTED_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Параметры подключения к провайдеру. */
    private final TwelveFreeConnectionProps props;

    /**
     * Конструктор.
     */
    public TwelveFreeFxSpotHandler(
            WebClient webClient,
            TwelveFreeConnectionProps props
    ) {
        // Конструктор материнского класса обработчика
        super(HANDLER_CODE, FxSpot.class, InstrumentType.FX_SPOT, SUPPORTED_CODES);

        Objects.requireNonNull(webClient, "webClient must not be null");
        Objects.requireNonNull(props, "props must not be null");

        this.webClient = webClient;
        this.props = props;
    }

    /**
     * Реализация получения котировки.
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Провайдер ожидает формат символа инструмента: BASE/QUOTE
        String instrumentSymbol = instrument.base().code() + "/" + instrument.quote().code();
        return webClient.get()
                .uri(b -> b
                        .path("/price")
                        .queryParam("symbol", instrumentSymbol)
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> responseJsonToQuoteTick(json, instrument.instrumentCode()))
                .flux();
    }

    /**
     * Преобразование JSON ответа провайдера в доменную модель котировки.
     */
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
