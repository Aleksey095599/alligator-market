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
 *     <li>Пул TCP-соединений для провайдеров {@link ConnectionProvider};</li>
 *     <li>Низкоуровневый reactor-netty {@link HttpClient} для провайдеров (поверх пула TCP-соединений).</li>
 * </ul>
 *
 * <p>Примечание: Поверх полученного {@link HttpClient} реализована единая конфигурация базового слоя WebClient
 * для всех провайдеров рыночных данных.</p>
 */
@Configuration(proxyBeanMethods = false)
public class GlobalProviderHttpConfig {

    /* Наименования бинов. */
    public static final String BEAN_CONNECTION_POOL = "providerConnectionPool";
    public static final String BEAN_HTTP_CLIENT = "providerHttpClient";

    /* Название пула TCP-соединений. */
    private static final String POOL_NAME = "provider-connection-pool";

    /**
     * <b>Пул TCP-соединений для провайдеров.</b>
     */
    @Bean(BEAN_CONNECTION_POOL)
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

    /**
     * <b>Низкоуровневый HTTP-клиент для провайдеров.</b>
     */
    @Bean(BEAN_HTTP_CLIENT)
    public HttpClient providerHttpClient(@Qualifier(BEAN_CONNECTION_POOL) ConnectionProvider cp) {

        return HttpClient.create(cp)
                // Лимит времени установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Лимит времени ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }
}
