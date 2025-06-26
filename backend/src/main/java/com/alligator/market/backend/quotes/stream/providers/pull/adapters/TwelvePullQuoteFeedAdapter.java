package com.alligator.market.backend.quotes.stream.providers.pull.adapters;

import com.alligator.market.backend.quotes.stream.providers.list_all.entity.Provider;
import com.alligator.market.backend.quotes.stream.providers.list_all.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.quotes.stream.providers.list_all.repository.ProviderRepository;
import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;
import com.alligator.market.domain.quotes.stream.ports.PullQuoteFeedPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Получение котировок от провайдера TwelveData.
 * Настройки подгружаются из списка провайдеров (таблица 'providers').
 */
@Service
@Slf4j
public class TwelvePullQuoteFeedAdapter implements PullQuoteFeedPort {

    private final String providerName;
    private final WebClient webClient;
    private final String apiKey;

    public TwelvePullQuoteFeedAdapter(@Value("${quotes.provider.twelve.pull.name}") String providerName,
                                      WebClient.Builder builder,
                                      ProviderRepository repository) {
        this.providerName = providerName;
        Provider provider = repository.findByName(providerName)
                .orElseThrow(() -> new ProviderNotFoundException(providerName));
        this.webClient = builder.baseUrl(provider.getBaseUrl()).build();
        this.apiKey = provider.getApiKey();
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
                            .queryParam("apikey", apiKey)
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
                    providerName
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
