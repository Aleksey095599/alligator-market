package com.alligator.market.backend.source.config.infra.handler.web.http.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class HandlerBaseConnectionPoolConfig {
    public static final String BEAN_HANDLER_BASE_CONNECTION_POOL = "handlerBaseConnectionPool";
    private static final String POOL_NAME = "handler-connection-pool";

    @Bean(value = BEAN_HANDLER_BASE_CONNECTION_POOL, destroyMethod = "dispose")
    public ConnectionProvider handlerBaseConnectionPool() {
        return ConnectionProvider.builder(POOL_NAME)

                .maxConnections(100)

                .pendingAcquireMaxCount(1_000)

                .pendingAcquireTimeout(Duration.ofSeconds(10))

                .maxIdleTime(Duration.ofSeconds(30))
                .build();
    }
}
