package com.alligator.market.backend.provider.adapter.profinance.config;

import com.alligator.market.backend.provider.config.http.ProviderHttpConfigGlobal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Конфигурационный класс создает WebClient для адаптера провайдера рыночных данных ProFinance (парсинг с сайта).
 * Использует общий для всех провайдеров HTTP-клиент {@link ProviderHttpConfigGlobal}
 * и настройки подключения для данного провайдера {@link ProFinanceAdapterProps}.
 */
@Configuration
public class ProFinanceWebConfig {

    /* Параметры подключения к провайдеру. */
    private final ProFinanceAdapterProps props;

    /* Общий HTTP-клиент провайдеров. */
    private final HttpClient httpClient;

    /**
     * Конструктор.
     */
    public ProFinanceWebConfig(
            ProFinanceAdapterProps props,
            @Qualifier("providerHttpClient") HttpClient httpClient // <-- инжекция единого HTTP-клиента для провайдеров

    ) {
        this.httpClient = httpClient;
        this.props = props;
    }

    /**
     * Бин web-клиента провайдера ProFinance.
     */
    @Bean("proFinanceWebClient")
    public WebClient proFinanceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(props.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("User-Agent", "Alligator Market")
                .build();
    }
}
