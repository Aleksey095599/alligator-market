package com.alligator.market.backend.provider.infra.handler.web;

import com.alligator.market.backend.provider.infra.handler.web.http.BaseProviderHttpConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Базовая конфигурация wiring web-клиента для всех провайдеров рыночных данных.
 *
 * <p>Примечание: {@link BaseProviderWebClientConfig} является "шаблоном" для частных конфигураций web-клиентов
 * обработчиков финансовых инструментов (через {@link WebClient#mutate()}).</p>
 */
@Configuration(proxyBeanMethods = false)
@Import(BaseProviderHttpConfig.class)
public class BaseProviderWebClientConfig {

    /* Наименования бина. */
    public static final String BEAN_NAME = "providerBaseWebClient";

    /**
     * Бин базового web-клиента для всех провайдеров.
     *
     * @param httpClient базовый низкоуровневый reactor-netty HTTP-клиент для всех провайдеров
     */
    @Bean(BEAN_NAME)
    public WebClient providerBaseWebClient(
            @Qualifier(BaseProviderHttpConfig.BEAN_HTTP_CLIENT) HttpClient httpClient
    ) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // .filter(...)
                .build();
    }
}


