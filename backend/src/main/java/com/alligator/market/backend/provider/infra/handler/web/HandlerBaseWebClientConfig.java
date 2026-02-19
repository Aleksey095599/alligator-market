package com.alligator.market.backend.provider.infra.handler.web;

import com.alligator.market.backend.provider.infra.handler.web.http.HandlerBaseHttpClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Конфигурация базового web-клиента для всех обработчиков финансовых инструментов.
 *
 * <p>Примечание: конфигурации web-клиентов конкретных обработчиков выстраиваются поверх базовой конфигурации
 * через {@link WebClient#mutate()}.</p>
 */
@Configuration(proxyBeanMethods = false)
@Import({HandlerBaseHttpClientConfig.class})
public class HandlerBaseWebClientConfig {

    public static final String BEAN_NAME = "handlerBaseWebClient";

    /**
     * Бин базового web-клиента всех обработчиков.
     *
     * @param handlerBaseHttpClient базовый HTTP-клиент всех обработчиков
     */
    @Bean(BEAN_NAME)
    public WebClient handlerBaseWebClient(
            @Qualifier(HandlerBaseHttpClientConfig.BEAN_NAME)
            HttpClient handlerBaseHttpClient
    ) {
        return WebClient.builder()
                // Подключаем WebClient к базовому HTTP-клиенту
                .clientConnector(new ReactorClientHttpConnector(handlerBaseHttpClient))
                .build();
    }
}

