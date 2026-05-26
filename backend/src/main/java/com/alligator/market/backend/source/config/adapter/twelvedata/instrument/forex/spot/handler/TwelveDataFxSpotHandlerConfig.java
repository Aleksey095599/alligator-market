package com.alligator.market.backend.source.config.adapter.twelvedata.instrument.forex.spot.handler;

import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandler;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandlerPolicy;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.properties.TwelveDataFxSpotConnectionProperties;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.properties.TwelveDataFxSpotHandlerProperties;
import com.alligator.market.backend.source.config.adapter.twelvedata.instrument.forex.spot.web.TwelveDataFxSpotWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
@Import(TwelveDataFxSpotWebClientConfig.class)
@EnableConfigurationProperties(TwelveDataFxSpotHandlerProperties.class)
public class TwelveDataFxSpotHandlerConfig {
    public static final String BEAN_NAME = "twelveDataFxSpotHandler";

    @Bean(BEAN_NAME)
    public TwelveDataFxSpotHandler twelveDataFxSpotHandler(
            @Qualifier(TwelveDataFxSpotWebClientConfig.BEAN_NAME) WebClient webClient,
            TwelveDataFxSpotConnectionProperties connectionProps,
            TwelveDataFxSpotHandlerProperties handlerProps
    ) {
        return new TwelveDataFxSpotHandler(
                webClient,
                connectionProps.apiKey(),
                new TwelveDataFxSpotHandlerPolicy(handlerProps.pollInterval())
        );
    }
}
