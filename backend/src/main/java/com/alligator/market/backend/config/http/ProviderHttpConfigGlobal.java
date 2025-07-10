package com.alligator.market.backend.config.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Общая для всех провайдеров конфигурация низкоуровневого сетевого слоя: HTTP-клиент и пул TCP-соединений.
 */
@Configuration
public class ProviderHttpConfigGlobal {

    /**
     * Общий для всех провайдеров HTTP-клиент, поверх которого могут быть реализованы более высокоуровневые
     * сетевые абстракции, например, web-клиент.
     */
    @Bean("providerHttpClient")
    public HttpClient providerHttpClient(@Qualifier("providerConnectionPool") ConnectionProvider cp) {

        return HttpClient.create(cp)
                // Лимит времени установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Лимит времени ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }

    /**
     * Общий для всех провайдеров пул TCP-соединений, поверх которого реализован HTTP-клиент.
     */
    @Bean("providerConnectionPool")
    public ConnectionProvider providerConnectionPool() {

        return ConnectionProvider.builder("provider-connection-pool")
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
