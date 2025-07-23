package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeProps;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.provider.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static com.alligator.market.domain.instrument.type.InstrumentType.CURRENCY_PAIR;

/**
 * Адаптер для провайдера TwelveData (free).
 */
@Component
@Slf4j
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    private static final ProviderProfile PROFILE = new ProviderProfile(
            "TWELVE_FREE",
            "TwelveData (free)",
            Set.of(InstrumentType.CURRENCY_PAIR),
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false,
            1_000
    );

    private final TwelveFreeProps props;
    private final WebClient webClient;

    public TwelveFreeAdapterV2(
            TwelveFreeProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient // инжекция bean web-клиента для TwelveData
    ) {
        this.props = props;
        this.webClient = webClient;
    }

    /** Профиль провайдера. */
    @Override
    public ProviderProfile profile() { return PROFILE; }

    /** Реактивный поток котировок. */
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
                profile().providerCode()
        );
    }
}
