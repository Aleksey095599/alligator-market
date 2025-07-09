package com.alligator.market.backend.config.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Глобальный сетевой каркас для всех провайдеров рыночных данных.
 */
@Configuration
public class ProviderHttpConfigGlobal {

    /**
     * Единый пул TCP-соединений (Netty-каналов) для всех REST/WS-клиентов провайдеров.
     */
    @Bean("providerConnectionPool")
    public ConnectionProvider providerConnectionPool() {

        return ConnectionProvider.builder("provider-pool")
                // Верхний предел одновременно открытых TCP-соединений
                .maxConnections(100)
                // Сколько запросов может одновременно ждать свободного TCP-соединения
                .pendingAcquireMaxCount(1_000)
                // Таймаут ожидания свободного TCP-соединения
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                // Время простоя TCP-соединения (без активности), далее канал закрывается
                .maxIdleTime(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Базовый HTTP-клиент (Reactor Netty) для всех провайдеров.
     * Все запросы провайдеров в рамках этого HTTP-клиента используют единый пул TCP-соединений.
     */
    @Bean("providerHttpClient")
    public HttpClient providerHttpClient(
            @Qualifier("providerConnectionPool")
            ConnectionProvider cp
    ) {

        return HttpClient.create(cp)
                // Таймаут установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Таймаут ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }
}
