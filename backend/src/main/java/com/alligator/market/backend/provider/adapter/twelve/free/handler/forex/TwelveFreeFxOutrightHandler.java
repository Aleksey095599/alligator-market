package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.contract.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;

/** Обработчик (handler) инструмента FX_OUTRIGHT для TwelveData (free). */
public class TwelveFreeFxOutrightHandler implements InstrumentHandler {

    // Переменная с кодом провайдера к которому относится данный обработчик
    private static final String PROVIDER_CODE = "TWELVE_FREE";

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

    /** Возвращает код провайдера рыночных данных, к которому относится обработчик. */
    @Override
    public String getProviderCode() {
        return PROVIDER_CODE;
    }

    /** Возвращает поддерживаемый тип инструмента. */
    @Override
    public InstrumentType getInstrumentType() {
        return InstrumentType.FX_OUTRIGHT;
    }


    /** Возвращает котировку для указанного инструмента. */
    @Override
    public Flux<QuoteTick> getInstrumentQuote(Instrument instrument) {

        FxOutright fxOutright = (FxOutright) instrument;

        // Провайдер ожидает именно такой формат запрашиваемого символа инструмента:
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
