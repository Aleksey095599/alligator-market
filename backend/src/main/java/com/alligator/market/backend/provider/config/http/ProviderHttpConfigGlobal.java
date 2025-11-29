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
 * <p>В данном конфигурационном классе задается Spring-бин пула TCP-соединений, затем поверх него выстраивается
 * Spring-бин HTTP-клиента.</p>
 * <p>Созданный HTTP-клиент является единым для всех провайдеров.</p>
 */
@Configuration
public class ProviderHttpConfigGlobal {

    /**
     * <b>Единый пул TCP-соединений, используемый всеми провайдерами рыночных данных.</b>
     *
     * <p>Поверх данного пула TCP-соединений будет реализован единый HTTP-клиент.</p>
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
     * <b>Единый низкоуровневый HTTP-клиент для всех провайдеров рыночных данных.</b>
     *
     * <p>Поверх данного HTTP-клиента могут быть реализованы более высокоуровневые сетевые абстракции (в частности, web-клиент).</p>
     * <p>Данный HTTP-клиент использует единый для всех провайдеров пул TCP-соединений.</p>
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
