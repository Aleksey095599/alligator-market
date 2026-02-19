package com.alligator.market.backend.provider.infra.handler.web.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Базовая конфигурация низкоуровневого reactor-netty HTTP-клиента для всех провайдеров рыночных данных.
 */
@Configuration(proxyBeanMethods = false)
public class BaseProviderHttpClientConfig {

    /* Наименование бина reactor-netty HTTP-клиента. */
    public static final String BEAN_HTTP_CLIENT = "providerHttpClient";

    /**
     * Бин низкоуровневого reactor-netty HTTP-клиента для всех провайдеров.
     *
     * @param connectionProvider пул TCP-соединений
     */
    @Bean(BEAN_HTTP_CLIENT)
    public HttpClient providerHttpClient(
            @Qualifier(BaseProviderConnectionPoolConfig.BEAN_CONNECTION_POOL) ConnectionProvider connectionProvider
    ) {

        return HttpClient.create(connectionProvider)
                // Лимит времени установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Лимит времени ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }
}
