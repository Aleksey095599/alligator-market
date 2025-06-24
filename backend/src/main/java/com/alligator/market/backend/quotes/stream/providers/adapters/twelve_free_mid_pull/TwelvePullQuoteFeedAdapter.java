package com.alligator.market.backend.quotes.stream.providers.adapters.twelve_free_mid_pull;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;
import com.alligator.market.domain.quotes.stream.ports.QuoteFeedPort;
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
public class TwelvePullQuoteFeedAdapter implements QuoteFeedPort {

    private static final String PROVIDER = "TWELVEDATA";

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

        String symbol = pairCode.substring(0, 3) + "/" + pairCode.substring(3);

        try {
            PriceDto dto = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/price")
                            .queryParam("symbol", symbol)
                            .queryParam("apikey", properties.getApikey())
                            .build())
                    .retrieve()
                    .bodyToMono(PriceDto.class)
                    .block();

            if (dto == null || dto.price() == null) {
                throw new IllegalStateException("Empty price");
            }
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

    /* Объект для ответа провайдера (https://api.twelvedata.com/price) в виде одной записи с ценой. */
    private record PriceDto(String price) {
    }

}
