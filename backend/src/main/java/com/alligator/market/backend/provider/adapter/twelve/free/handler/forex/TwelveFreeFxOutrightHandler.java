package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import com.alligator.market.domain.provider.model.InstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;

/** Обработчик котировок FX-спот для TwelveData (free). */
public class TwelveFreeFxOutrightHandler implements InstrumentHandler {

    private final WebClient webClient;
    private final TwelveFreeConnectionProps props;
    private final String providerCode;

    public TwelveFreeFxOutrightHandler(
            WebClient webClient,
            TwelveFreeConnectionProps props,
            String providerCode
    ) {
        this.webClient = webClient;
        this.props = props;
        this.providerCode = providerCode;
    }

    /** Возвращает поддерживаемый тип инструмента. */
    @Override
    public InstrumentType supportedInstrument() {
        return InstrumentType.FX_OUTRIGHT;
    }


    /** Возвращает котировку для указанного инструмента. */
    @Override
    public Flux<QuoteTick> instrumentQuote(Instrument instrument) {

        FxOutright fxOutright = (FxOutright) instrument;

        // Провайдер ожидает именно такой формат запроса:
        String symbol = fxOutright.baseCurrency() + "/" + fxOutright.quoteCurrency();

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
                providerCode
        );
    }
}

