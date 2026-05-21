package com.alligator.market.backend.source.config.infra.handler.web.http;

import com.alligator.market.backend.source.config.infra.handler.web.http.tcp.HandlerBaseConnectionPoolConfig;
import com.alligator.market.backend.source.infra.handler.web.http.properties.HandlerWebClientProperties;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration(proxyBeanMethods = false)
@Import(HandlerBaseConnectionPoolConfig.class)
@EnableConfigurationProperties(HandlerWebClientProperties.class)
public class HandlerBaseHttpClientConfig {
    public static final String BEAN_HANDLER_BASE_HTTP_CLIENT = "handlerBaseHttpClient";

    @Bean(BEAN_HANDLER_BASE_HTTP_CLIENT)
    public HttpClient handlerBaseHttpClient(
            @Qualifier(HandlerBaseConnectionPoolConfig.BEAN_HANDLER_BASE_CONNECTION_POOL)
            ConnectionProvider handlerBaseConnectionPool,
            HandlerWebClientProperties properties
    ) {
        return HttpClient.create(handlerBaseConnectionPool)

                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.connectTimeoutMillis())

                .responseTimeout(properties.responseTimeout());
    }
}
