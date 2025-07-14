package com.alligator.market.backend.provider.twelve.free.config.web;

import com.alligator.market.backend.provider.twelve.free.config.TwelveFreeConnectionProps;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Конфигурационный класс, который создает и настраивает WebClient для работы с API TwelveData.
 * Использует общий для всех провайдеров HTTP-клиент и частные настройки провайдера.
 */
@Configuration
public class TwelveFreeWebConfig {

    private final TwelveFreeConnectionProps props;
    private final HttpClient httpClient;

    // Конструктор с инжекцией общего http-клиента для всех провайдеров
    public TwelveFreeWebConfig(
            TwelveFreeConnectionProps props,
            @Qualifier("providerHttpClient") HttpClient httpClient
    ) {
        this.props = props;
        this.httpClient = httpClient;
    }

    /**
     * Строим бин web-клиента для провайдера TwelveData.
     */
    @Bean("twelveFreeWebClient")
    WebClient twelveFreeWebClient() {

        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("User-Agent", "Alligator Market Backend")
                .build();
    }
}
