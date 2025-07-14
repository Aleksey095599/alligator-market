package com.alligator.market.backend.provider.twelve.free.web;

import com.alligator.market.backend.provider.twelve.free.TwelveFreeProps;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
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

    private final TwelveFreeProps props;
    private final HttpClient httpClient;
    private final BuildProperties build;

    // Конструктор с инжекцией общего http-клиента для всех провайдеров
    public TwelveFreeWebConfig(
            TwelveFreeProps props,
            @Qualifier("providerHttpClient") HttpClient httpClient,
            BuildProperties build
    ) {
        this.props = props;
        this.httpClient = httpClient;
        this.build = build;
    }

    /**
     * Строим бин web-клиента для провайдера TwelveData.
     */
    @Bean("twelveFreeWebClient")
    WebClient twelveFreeWebClient() {

        String ua = build.getName() + "/" + build.getVersion();

        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("User-Agent", ua)
                .build();
    }
}
