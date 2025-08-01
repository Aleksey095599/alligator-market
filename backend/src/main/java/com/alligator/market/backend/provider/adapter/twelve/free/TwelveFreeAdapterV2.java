package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static com.alligator.market.domain.instrument.InstrumentType.CURRENCY_PAIR;

/**
 * Адаптер для провайдера TwelveData (free).
 */
@Component
@Slf4j
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    /** Внутренний контракт для работы с конкретным инструментом конкретно для данного провайдера. */
    @FunctionalInterface
    private interface InstrumentHandler {
        Mono<QuoteTick> fetch(Instrument instrument);
    }

    // Параметры подключения к провайдеру. Автоматически считываются из настроек приложения.
    private final TwelveFreeConnectionProps props;
    // Веб клиент провайдера
    private final WebClient webClient;
    // Карта: тип инструмента <--> внутренний контракт
    private final Map<InstrumentType, InstrumentHandler> handlers = new EnumMap<>(InstrumentType.class);
    // Назначаем реализацию внутреннего контракта
    private final InstrumentHandler fxSpotHandler = this::fetchFxSpot;

    /**
     * Конструктор.
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        this.props = props;
        this.webClient = webClient;
        handlers.put(InstrumentType.CURRENCY_PAIR, fxSpotHandler);
    }

    //===================
    // Профиль провайдера
    //===================
    /** Переопределяем метод, который возвращает профиль провайдера. */
    @Override
    public ProviderProfile profile() {
        return new ProviderProfile(
                "TWELVE_FREE",
                "TwelveData (free)",
                Set.of(InstrumentType.CURRENCY_PAIR),
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                60_000
        );
    }

    //===========================
    // Реактивный поток котировок
    //===========================
    @Override
    public Flux<QuoteTick> streamQuotes(Instrument instrument) {

        // Извлекаем внутренний идентификатор инструмента
        final String internalCode = instrument.internalCode();

        // Биржевой идентификатор инструмента, требуемый провайдером
        final String pairCodeForRequest;

        // Для валютных пар данный провайдер ожидает формат биржевого идентификатора "EUR/USD"
        if (instrument.instrumentType() == CURRENCY_PAIR) {
            CurrencyPair pair = (CurrencyPair) instrument;
            pairCodeForRequest = pair.base() + "/" + pair.quote();
        } else {
            pairCodeForRequest = instrument.internalCode();
        }

        return webClient.get()
                .uri(builder -> builder
                        .path("/price")
                        .queryParam("symbol", pairCodeForRequest)
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> jsonToQuoteTick(json, internalCode))
                .flux();
    }

    //-----------------------
    // Вспомогательные методы
    //-----------------------

    /* Метод преобразования ответа провайдера к модели тика котировки */
    private QuoteTick jsonToQuoteTick(JsonNode json, String pairCodeByModel) {

        // Извлекаем значение поля "price" из JSON-ответа провайдера в виде объекта JsonNode
        JsonNode priceNode = json.get("price");

        if (priceNode == null) {
            throw new IllegalArgumentException("Invalid provider response: " + json);
        }

        BigDecimal price = new BigDecimal(priceNode.asText());

        return new QuoteTick(
                pairCodeByModel,
                price,
                price,
                Instant.now(),
                profile().providerCode()
        );
    }
}
