package com.alligator.market.backend.fx.quote.collector.twelvedata;

import com.alligator.market.core.fx.model.CurrencyQuote;
import com.alligator.market.core.fx.port.ExternalPriceFeed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Тестовый адаптер для получения котировок валют через TwelveData API.
 * Реализует интерфейс ExternalPriceFeed для интеграции с внешним источником котировок.
 */
@Component
@Slf4j
public class TwelveDataFeedAdapter implements ExternalPriceFeed {

    // Настройки подключения к TwelveData API
    @Value("${twelvedata.apikey}")
    private String apiKey;
    @Value("${twelvedata.poll-period-ms:1000}")
    private long pollPeriodMs;

    // Временный выбор валютной пары (TODO: реализовать выбор пары из базы списка валютных пар)
    private static final Map<String, Long> SYMBOL_TO_ID = Map.of(
            "EUR/USD", 4L
    );

    // HTTP-клиент для отправки запросов к TwelveData API
    private final WebClient client =
            WebClient.builder().baseUrl("https://api.twelvedata.com").build();

    /**
     * Создает поток котировок валют, периодически опрашивая TwelveData API
     */
    @Override
    public Flux<CurrencyQuote> streamAll() {
        return Flux.interval(Duration.ZERO, Duration.ofMillis(pollPeriodMs))
                .flatMap(t -> Flux.fromIterable(SYMBOL_TO_ID.entrySet()))
                .flatMap(entry -> fetchPrice(entry.getKey(), entry.getValue()));
    }

    /* Запрашивает актуальную цену для указанной валютной пары. */
    private Flux<CurrencyQuote> fetchPrice(String symbol, Long pairId) {
        return client.get()
                .uri(uri -> uri.path("/price")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(PriceDto.class)
                .flatMap(dto -> {
                    if (dto.price() == null) {
                        return Mono.empty();
                    }
                    return Mono.just(new CurrencyQuote(
                            pairId,
                            new BigDecimal(dto.price()),       // bid = ask = price
                            new BigDecimal(dto.price()),       // bid = ask = price
                            (short) 2,                         // priority (secondary)
                            Instant.now()));
                })
                .flux();
    }

    /** Мини-DTO to serialize ответ TwelveData API */
    private record PriceDto(
            // "https://api.twelvedata.com/price" возвращает JSON {"price":"1.10034"}
            String price
    ) {}

}
