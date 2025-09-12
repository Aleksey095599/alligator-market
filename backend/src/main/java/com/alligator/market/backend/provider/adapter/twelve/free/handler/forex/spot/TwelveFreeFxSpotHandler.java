package com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.twelve.free.TwelveFreeAdapterV2;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.provider.handler.contract.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Обработчик (handler) инструмента FX_SPOT для TwelveData (free).
 * Тип инструмента задаётся в родительском классе.
 */
public class TwelveFreeFxSpotHandler implements AbstractInstrumentHandler<TwelveFreeAdapterV2, FxSpot> {

    private static final String HANDLER_CODE = "TWELVE_FREE_FX_SPOT_HANDLER";
    private TwelveFreeAdapterV2 provider;
    private final Set<FxSpot> instruments;
    private static final Class<FxSpot> INSTRUMENT_CLASS = FxSpot.class;

    private final WebClient webClient;
    private final TwelveFreeConnectionProps props;

    // Конструктор
    public TwelveFreeFxSpotHandler(
            Set<FxSpot> instruments,
            WebClient webClient,
            TwelveFreeConnectionProps props
    ) {
        this.instruments = Set.copyOf(Objects.requireNonNull(instruments, "instruments must not be null"));
        this.webClient = Objects.requireNonNull(webClient, "webClient must not be null");
        this.props = Objects.requireNonNull(props, "props must not be null");
    }

    // Устанавливает провайдера для обработчика
    public void setProvider(TwelveFreeAdapterV2 provider) {
        this.provider = Objects.requireNonNull(provider, "provider must not be null");
    }

    /** Код обработчика. */
    @Override
    public String code() {
        return HANDLER_CODE;
    }

    /** Провайдер, к которому относится обработчик. */
    @Override
    public TwelveFreeAdapterV2 provider() {
        return provider;
    }

    /** Класс поддерживаемых инструментов. */
    @Override
    public Class<FxSpot> instrumentClass() {
        return INSTRUMENT_CLASS;
    }

    /** Набор поддерживаемых инструментов. */
    @Override
    public Set<FxSpot> supportedInstruments() {
        return instruments;
    }

    /**
     * Котировка заданного инструмента.
     */
    @Override
    public Publisher<QuoteTick> quote(FxSpot instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        if (!INSTRUMENT_CLASS.isInstance(instrument)) {
            throw new InstrumentWrongClassException(INSTRUMENT_CLASS, instrument.getClass());
        }
        if (!instruments.contains(instrument)) {
            throw new InstrumentNotSupportedException(
                    instrument.code(),
                    HANDLER_CODE,
                    provider.profile().providerCode()
            );
        }
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
                provider.profile().providerCode()
        );
    }
}
