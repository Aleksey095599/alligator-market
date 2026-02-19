package com.alligator.market.backend.provider.infra.handler.web.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Базовая конфигурация пула TCP-соединений для всех провайдеров рыночных данных.
 */
@Configuration(proxyBeanMethods = false)
public class BaseProviderConnectionPoolConfig {

    /* Наименование бина пула TCP-соединений. */
    public static final String BEAN_CONNECTION_POOL = "providerConnectionPool";

    /* Название пула TCP-соединений. */
    private static final String POOL_NAME = "provider-connection-pool";

    /**
     * Бин пула TCP-соединений для всех провайдеров.
     */
    @Bean(value = BEAN_CONNECTION_POOL, destroyMethod = "dispose")
    public ConnectionProvider providerConnectionPool() {

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
