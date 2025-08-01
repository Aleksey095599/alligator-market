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

/**
 * Адаптер для провайдера TwelveData (free).
 */
@Component
@Slf4j
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    // Параметры подключения к провайдеру. Автоматически считываются из настроек приложения.
    private final TwelveFreeConnectionProps props;
    // Веб клиент провайдера
    private final WebClient webClient;
    // Карта соответствия: тип инструмента <--> handler
    private final Map<InstrumentType, InstrumentHandler> handlerMap = new EnumMap<>(InstrumentType.class);

    /** Конструктор адаптера TwelveFreeAdapterV2. */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient // Инъекция нужного веб-клиента
    ) {
        this.props = props;
        this.webClient = webClient;

        // Добавляем handler для валютных пар
        handlerMap.put(InstrumentType.CURRENCY_PAIR, this::fetchFxSpot);
    }

    //===========================
    //    Профиль провайдера
    //===========================

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

    /** Переопределяем метод, который возвращает котировки. */
    @Override
    public Flux<QuoteTick> streamQuotes(Instrument instrument) {

        // Извлекаем handler для данного типа инструмента
        InstrumentHandler handler = handlerMap.get(instrument.instrumentType());

        if (handler == null) {
            return Flux.error(new UnsupportedOperationException(
                    "Instrument type " + instrument.instrumentType()
                            + " not supported by " + profile().providerCode()));
        }

        return handler.fetch(instrument).flux();
    }

    //===========================
    //  Вспомогательные методы
    //===========================

    /** Handler для работы с конкретным инструментом. */
    @FunctionalInterface
    private interface InstrumentHandler {
        Mono<QuoteTick> fetch(Instrument instrument);
    }

    /** Реализация handler для запросов котировок валютных пар. */
    private Mono<QuoteTick> fetchFxSpot(Instrument instrument) {

        CurrencyPair pair = (CurrencyPair) instrument;

        // Требуемый формат символа валютной пары для запроса котировки
        String symbol = pair.base() + "/" + pair.quote();

        return webClient.get()
                .uri(b -> b
                        .path("/price")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> jsonToQuoteTick(json, instrument.internalCode()));
    }

    /** Метод формирования котировки {@link QuoteTick} из JSON-ответа провайдера. */
    private QuoteTick jsonToQuoteTick(JsonNode json, String internalInstrumentCode) {

        // Извлекаем значение поля "price" из JSON-ответа провайдера
        JsonNode priceNode = json.get("price");

        if (priceNode == null) {
            throw new IllegalArgumentException("Invalid provider response: " + json);
        }

        BigDecimal price = new BigDecimal(priceNode.asText());

        return new QuoteTick(
                internalInstrumentCode,
                price,
                price,
                Instant.now(),
                profile().providerCode()
        );
    }
}
