package com.alligator.market.backend.marketdata.provider.infra.handler.web.http.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Конфигурация базового пула TCP-соединений для всех обработчиков финансовых инструментов.
 */
@Configuration(proxyBeanMethods = false)
public class HandlerBaseConnectionPoolConfig {

    public static final String BEAN_HANDLER_BASE_CONNECTION_POOL = "handlerBaseConnectionPool";
    private static final String POOL_NAME = "handler-connection-pool";

    /**
     * Бин базового пула TCP-соединений обработчиков.
     */
    @Bean(value = BEAN_HANDLER_BASE_CONNECTION_POOL, destroyMethod = "dispose")
    public ConnectionProvider handlerBaseConnectionPool() {

        return ConnectionProvider.builder(POOL_NAME)
                // Верхний предел одновременно открытых TCP-соединений
                .maxConnections(100)
                // Лимит количества запросов в ожидании свободного TCP-соединения
                .pendingAcquireMaxCount(1_000)
                // Лимит времени ожидания свободного TCP-соединения
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                // Лимит времени простоя TCP-соединения (без активности), далее канал закрывается
                .maxIdleTime(Duration.ofSeconds(30))
                .build();
    }
}
