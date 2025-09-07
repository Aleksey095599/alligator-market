package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex;

import com.alligator.market.backend.provider.adapter.twelve.free.TwelveFreeAdapterV2;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * Обработчик (handler) инструмента FX_SPOT для TwelveData (free).
 * Тип инструмента задаётся в родительском классе.
 */
public class TwelveFreeFxSpotHandler extends AbstractInstrumentHandler<TwelveFreeAdapterV2, FxSpot> {

    // Веб-клиент для запросов к провайдеру
    private final WebClient webClient;

    // Настройки подключения к провайдеру
    private final TwelveFreeConnectionProps props;

    // Конструктор
    public TwelveFreeFxSpotHandler(
            WebClient webClient,
            TwelveFreeConnectionProps props,
            Set<FxSpot> instruments
    ) {
        super(FxSpot.class, instruments);
        this.webClient = webClient;
        this.props = props;
    }


    /** Возвращает котировку для указанного инструмента. */
    @Override
    public Flux<QuoteTick> getInstrumentQuote(FxSpot instrument) {

        // Провайдер ожидает именно такой формат запрашиваемого символа инструмента:
        String symbol = instrument.base().code() + "/" + instrument.quote().code();

        return webClient.get()
                .uri(b -> b
                        .path("/price")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> responseJsonToQuoteTick(json, instrument.getCode()))
                .flux();
    }

    /** Метод преобразования JSON ответа провайдера в доменную модель котировки. */
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
                getProvider().getProfile().providerCode()
        );
    }
}
