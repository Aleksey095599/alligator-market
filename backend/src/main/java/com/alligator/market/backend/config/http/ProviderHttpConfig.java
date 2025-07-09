package com.alligator.market.backend.config.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Глобальный сетевой каркас для ВСЕХ адаптеров рыночных данных.
 */
@Configuration
public class ProviderHttpConfig {

    /**
     * Пул TCP-соединений, общий на все REST/WS-клиенты провайдеров.
     * Этот бин создает пул соединений со следующими параметрами:
     * - максимум 100 одновременных соединений
     * - до 1000 ожидающих запросов в очереди
     * - таймаут ожидания соединения 10 секунд
     * - максимальное время простоя соединения 30 секунд
     */
    @Bean("providerPool")
    public ConnectionProvider providerPool() {
        return ConnectionProvider.builder("provider-pool")
                .maxConnections(100)
                .pendingAcquireMaxCount(1_000)
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                .maxIdleTime(Duration.ofSeconds(30))
                .build();
    }


}
