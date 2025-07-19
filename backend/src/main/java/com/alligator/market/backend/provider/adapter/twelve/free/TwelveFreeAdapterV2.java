package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeProps;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.instrument.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static com.alligator.market.domain.instrument.InstrumentType.CURRENCY_PAIR;

/**
 * Реализация адаптера провайдера TwelveData (free plan).
 */
@Component
@Slf4j
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    private final TwelveFreeProps props;
    private final WebClient webClient;

    public TwelveFreeAdapterV2(
            TwelveFreeProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient // инжекция bean web-клиента для TwelveData
    ) {
        this.props = props;
        this.webClient = webClient;
    }

    //==================================
    // Статические метаданные провайдера
    //==================================

    @Override public String providerCode() {
        return "TWELVE_FREE";
    }
    @Override
    public String displayName() {
        return "TwelveData (free plan)";
    }
    @Override
    public Set<InstrumentType> instrumentTypes() {
        return Set.of(CURRENCY_PAIR);
    }
    @Override public DeliveryMode deliveryMode() {
        return DeliveryMode.PULL;
    }
    @Override public AccessMethod accessMethod() {
        return AccessMethod.API_POLL;
    }
    @Override public boolean supportsBulkSubscription() {
        return false;
    }
    @Override
    public Duration minPollPeriod() {
        return Duration.ofMinutes(1);
    }

    //===========================
    // Поток котировок провайдера
    //===========================

    /**
     * Поток котировок для заданного инструмента от провайдера TwelveData (free plan).
     */
    @Override
    public Flux<QuoteTick> streamQuotes(Instrument instrument) {

        // Биржевой идентификатор инструмента согласно модели данного приложения
        final String symbolByModel = instrument.symbol();
        // Биржевой идентификатор инструмента, требуемый провайдером
        final String symbolForRequest;

        // Для валютных пар данный провайдер ожидает формат биржевого идентификатора "EUR/USD"
        if (instrument.instrumentType() == CURRENCY_PAIR) {
            CurrencyPair pair = (CurrencyPair) instrument;
            symbolForRequest = pair.code1() + "/" + pair.code2();
        } else {
            symbolForRequest = instrument.symbol();
        }

        return webClient.get()
                .uri(builder -> builder
                        .path("/price")
                        .queryParam("symbol", symbolForRequest)
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> jsonToQuoteTick(json, symbolByModel))
                .flux();
    }

    //-----------------------
    // Вспомогательные методы
    //-----------------------

    /* Метод преобразования ответа провайдера к модели тика котировки */
    private QuoteTick jsonToQuoteTick(JsonNode json, String symbolByModel) {

        // Извлекаем значение поля "price" из JSON-ответа провайдера в виде объекта JsonNode
        JsonNode priceNode = json.get("price");

        if (priceNode == null) {
            throw new IllegalArgumentException("Invalid provider response: " + json);
        }

        BigDecimal price = new BigDecimal(priceNode.asText());

        return new QuoteTick(
                symbolByModel,
                price,
                price,
                Instant.now(),
                providerCode()
        );
    }
}
