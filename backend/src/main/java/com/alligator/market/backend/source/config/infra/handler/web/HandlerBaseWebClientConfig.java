package com.alligator.market.backend.source.config.infra.handler.web;

import com.alligator.market.backend.source.config.infra.handler.web.http.HandlerBaseHttpClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Base WebClient configuration for all instrument handlers.
 *
 * <p>Specific handler WebClients are derived from this base client with {@link WebClient#mutate()}.</p>
 */
@Configuration(proxyBeanMethods = false)
@Import({HandlerBaseHttpClientConfig.class})
public class HandlerBaseWebClientConfig {

    public static final String BEAN_HANDLER_BASE_WEB_CLIENT = "handlerBaseWebClient";

    /**
     * Base WebClient bean for all handlers.
     *
     * @param handlerBaseHttpClient base HTTP client for all handlers
     */
    @Bean(BEAN_HANDLER_BASE_WEB_CLIENT)
    public WebClient handlerBaseWebClient(
            @Qualifier(HandlerBaseHttpClientConfig.BEAN_HANDLER_BASE_HTTP_CLIENT)
            HttpClient handlerBaseHttpClient
    ) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(handlerBaseHttpClient))
                .build();
    }
}
