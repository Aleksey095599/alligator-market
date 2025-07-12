package com.alligator.market.backend.provider.twelve.free;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

/**
 * Реализация адаптера провайдера TwelveData (free plan).
 */
@Component
@Slf4j
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    private final TwelveFreeProps props;
    private final WebClient webClient;

    /* Конструктор с инжекцией web-клиента для TwelveData */
    public TwelveFreeAdapterV2(
            TwelveFreeProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        this.props = props;
        this.webClient = webClient;
    }

    // Статические метаданные
    @Override public String providerCode() {
        return "TWELVE_FREE_PLAN";
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

    /**
     * Метод реализует поток котировок для заданного провайдера: обращается к соответствущему endpoint
     * провайдера, возвращает цену, преобразует цену от провайдера к модели тика котировки.
     */
    @Override
    public Flux<QuoteTick> streamQuotes(Instrument instrument) {

        URI uri = UriComponentsBuilder
                .fromPath("/price")
                .queryParam("symbol", instrument.symbol())
                .queryParam("apikey", props.apiKey())
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::jsonToTick)
                .flux();
    }

    /* Вспомогательный метод преобразования ответа провайдера к модели тика котировки */
    private QuoteTick jsonToTick(JsonNode json) {

        BigDecimal price = new BigDecimal(json.get("price").asText());

        return new QuoteTick(
                json.get("symbol").asText(),
                price,
                price,
                Instant.now(),
                providerCode()
        );
    }
}
