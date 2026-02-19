package com.alligator.market.backend.provider.infra.web.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Базовая конфигурация wiring низкоуровневого сетевого слоя для всех провайдеров рыночных данных.
 *
 * <p>Описание: В данном конфигурационном классе задаются:</p>
 * <ul>
 *     <li>Бин пула TCP-соединений для всех провайдеров;</li>
 *     <li>Бин низкоуровневого reactor-netty HTTP-клиента для всех провайдеров.</li>
 * </ul>
 *
 * <p>Назначение: Поверх полученного {@link #providerHttpClient} реализована базовая wiring конфигурация web-клиента
 * для всех провайдеров рыночных данных.</p>
 */
@Configuration(proxyBeanMethods = false)
public class BaseProviderHttpConfig {

    /* Наименования бинов. */
    public static final String BEAN_CONNECTION_POOL = "providerConnectionPool";
    public static final String BEAN_HTTP_CLIENT = "providerHttpClient";

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

    /**
     * Бин низкоуровневого reactor-netty HTTP-клиента для всех провайдеров.
     *
     * @param connectionProvider пул TCP-соединений
     */
    @Bean(BEAN_HTTP_CLIENT)
    public HttpClient providerHttpClient(@Qualifier(BEAN_CONNECTION_POOL) ConnectionProvider connectionProvider) {

        return HttpClient.create(connectionProvider)
                // Лимит времени установления TCP-соединения (handshake)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                // Лимит времени ожидания первого байта ответа после отправки HTTP-запроса
                .responseTimeout(Duration.ofSeconds(6));
    }
}
