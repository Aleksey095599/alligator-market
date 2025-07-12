package com.alligator.market.backend.provider.twelve.free;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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

        return webClient.get()
                .uri(uri -> uri.path("/price")
                        .queryParam("symbol", instrument.symbol())
                        .queryParam("apikey", props.apiKey())
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError, resp -> resp.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new IllegalStateException(
                                "HTTP " + resp.statusCode() + ": " + body))))
                .bodyToMono(JsonNode.class)
                .map(this::validate)
                .map(this::jsonToTick)
                .doOnError(e -> log.error("Failed to fetch quote for {}", instrument.symbol(), e))
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

    /* Проверка ответа провайдера и возврат JsonNode с ценой */
    private JsonNode validate(JsonNode json) {
        if (json.hasNonNull("code") && json.has("message")) {
            throw new IllegalStateException("Provider error: " + json.get("message").asText());
        }
        if (!json.hasNonNull("price")) {
            throw new IllegalStateException("Missing price field");
        }
        return json;
    }
}
