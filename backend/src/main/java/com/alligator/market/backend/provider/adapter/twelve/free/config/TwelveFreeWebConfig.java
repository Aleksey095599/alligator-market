package com.alligator.market.backend.provider.adapter.twelve.free.config;

import com.alligator.market.backend.provider.adapter.twelve.free.TwelveFreeAdapterV2;
import com.alligator.market.backend.provider.config.http.ProviderHttpConfigGlobal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Конфигурационный класс создает WebClient для адаптера провайдера рыночных данных {@link TwelveFreeAdapterV2}.
 * Использует общий для всех провайдеров HTTP-клиент {@link ProviderHttpConfigGlobal} и настройки
 * подключения {@link TwelveFreeConnectionProps}.
 */
@Configuration
public class TwelveFreeWebConfig {
    /* Базовые атрибуты конфигурации. */
    private final TwelveFreeConnectionProps props;
    private final HttpClient httpClient;

    /**
     * Конструктор.
     */
    public TwelveFreeWebConfig(
            TwelveFreeConnectionProps props,
            @Qualifier("providerHttpClient") HttpClient httpClient // <-- инжекция bean общего http-клиента провайдеров
    ) {
        this.props = props;
        this.httpClient = httpClient;
    }

    /**
     * Строим бин web-клиента для провайдера.
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
