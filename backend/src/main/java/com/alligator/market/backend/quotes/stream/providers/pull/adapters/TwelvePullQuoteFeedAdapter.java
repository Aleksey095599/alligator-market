package com.alligator.market.backend.quotes.stream.providers.pull.adapters;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;
import com.alligator.market.domain.quotes.stream.ports.PullQuoteFeedPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Получение котировок через сервис TwelveData.
 */
@Service
@Slf4j
public class TwelvePullQuoteFeedAdapter implements PullQuoteFeedPort {

    private static final String PROVIDER = "twelve_free_mid_pull";

    private final WebClient webClient;
    private final TwelvePullProperties properties;

    public TwelvePullQuoteFeedAdapter(WebClient.Builder builder, TwelvePullProperties properties) {
        this.webClient = builder.baseUrl(properties.getBaseUrl()).build();
        this.properties = properties;
    }

    //=======================================
    // Возвратить последнюю котировку по паре
    //=======================================
    @Override
    public QuoteTick fetchQuote(String pairCode) throws QuoteUnavailableException {

        // Для запроса требуется представление пары в виде CCY/CCY
        String symbol = pairCode.substring(0, 3) + "/" + pairCode.substring(3);

        try {
            // Выполняем HTTP GET запрос к TwelveData API для получения цены
            PriceDto dto = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/price")
                            .queryParam("symbol", symbol)
                            .queryParam("apikey", properties.getApikey())
                            .build())
                    .retrieve()
                    .bodyToMono(PriceDto.class)
                    .block();

            // Проверка полученного объекта
            if (dto == null || dto.price() == null) {
                throw new IllegalStateException("Empty price");
            }

            // Преобразование строкового представления цены в BigDecimal
            BigDecimal price = new BigDecimal(dto.price());

            // Формируем объект тика согласно доменной модели.
            // Временная реализация bid=ask из-за ограниченной подписки.
            return new QuoteTick(
                    pairCode,
                    price,
                    price,
                    Instant.now(),
                    PROVIDER
            );

        } catch (Exception e) {
            log.error("Cannot fetch quote from TwelveData", e);
            throw new QuoteUnavailableException("Failed to fetch quote", e);
        }
    }

    //-----------------------
    // Вспомогательные классы
    //-----------------------

    /* Объект, в который принимаем ответ от провайдера. */
    private record PriceDto(String price) {
    }

}
