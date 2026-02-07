package com.alligator.market.backend.provider.config.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * <b>Единая конфигурация низкоуровневого сетевого слоя для всех провайдеров рыночных данных.</b>
 *
 * <p>В данном конфигурационном классе задаются Spring-бины:</p>
 * <ul>
 *     <li>Пул TCP-соединений для провайдеров;</li>
 *     <li>Низкоуровневый HTTP-клиент (поверх пула TCP-соединений) для провайдеров.</li>
 * </ul>
 *
 * <p>Поверх заданного HTTP-клиента для провайдеров в приложении реализованы более высокоуровневые сетевые абстракции,
 * в частности, web-клиенты для провайдеров.</p>
 */
@Configuration
public class ProviderHttpConfigGlobal {

    /**
     * <b>Пул TCP-соединений для провайдеров.</b>
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

    /**
     * <b>Низкоуровневый HTTP-клиент для провайдеров.</b>
     */
    @Bean("providerHttpClient")
    public HttpClient providerHttpClient(@Qualifier("providerConnectionPool") ConnectionProvider cp) {

        return HttpClient.create(cp)
                // Лимит времени установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Лимит времени ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }
}
