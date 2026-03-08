package com.alligator.market.backend.marketdata.provider.infra.handler.web.http;

import com.alligator.market.backend.marketdata.provider.infra.handler.web.http.tcp.HandlerBaseConnectionPoolConfig;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Конфигурация базового низкоуровневого reactor-netty HTTP-клиента для всех обработчиков финансовых инструментов.
 */
@Configuration(proxyBeanMethods = false)
@Import(HandlerBaseConnectionPoolConfig.class)
public class HandlerBaseHttpClientConfig {

    public static final String BEAN_HANDLER_BASE_HTTP_CLIENT = "handlerBaseHttpClient";

    /**
     * Бин базового HTTP-клиента всех обработчиков.
     *
     * @param handlerBaseConnectionPool базовый пул TCP-соединений всех обработчиков
     */
    @Bean(BEAN_HANDLER_BASE_HTTP_CLIENT)
    public HttpClient handlerBaseHttpClient(
            @Qualifier(HandlerBaseConnectionPoolConfig.BEAN_HANDLER_BASE_CONNECTION_POOL)
            ConnectionProvider handlerBaseConnectionPool
    ) {

        return HttpClient.create(handlerBaseConnectionPool)
                // Лимит времени установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Лимит времени ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }
}
