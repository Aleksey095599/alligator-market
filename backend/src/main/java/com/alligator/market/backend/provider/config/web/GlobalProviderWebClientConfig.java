package com.alligator.market.backend.provider.config.web;

import com.alligator.market.backend.provider.config.http.GlobalProviderHttpConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Единая для всех провайдеров рыночных данных конфигурация базового слоя web-клиента.
 *
 * <p>В данном конфигурационном классе задаются Spring-бины:</p>
 * <ul>
 *     <li>Базовый {@link WebClient} для всех провайдеров.</li>
 * </ul>
 *
 * <p>Базовый {@link WebClient} используется как "шаблон" при создании конфигураций web-клиентов для
 * обработчиков финансовых инструментов через {@link WebClient#mutate()}.</p>
 */
@Configuration(proxyBeanMethods = false)
@Import(GlobalProviderHttpConfig.class)
public class GlobalProviderWebClientConfig {

    /* Наименования бина. */
    public static final String BEAN_BASE_WEB_CLIENT = "providerBaseWebClient";

    /**
     * Создаёт базовый {@link WebClient} для всех провайдеров.
     *
     * @param httpClient низкоуровневый reactor-netty HTTP-клиент для всех провайдеров
     */
    @Bean(BEAN_BASE_WEB_CLIENT)
    public WebClient providerBaseWebClient(
            @Qualifier(GlobalProviderHttpConfig.BEAN_HTTP_CLIENT) HttpClient httpClient
    ) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // Здесь место для общих настроек:
                // .filter(...)
                // .exchangeStrategies(...)
                // .defaultHeader(...)
                .build();
    }
}


