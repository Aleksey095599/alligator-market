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
 * <p>В данном конфигурационном классе задается Spring-бин базового {@link WebClient} для всех провайдеров,
 * который является "шаблоном" для создания конфигураций web-клиентов обработчиков финансовых инструментов
 * (через {@link WebClient#mutate()}).</p>
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
                // .filter(...)
                .build();
    }
}


