package com.alligator.market.backend.source.config.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.backend.source.config.adapter.moex.iss.instrument.forex.spot.web.MoexIssFxSpotWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
@Import(MoexIssFxSpotWebClientConfig.class)
public class MoexIssFxSpotHandlerConfig {
    public static final String BEAN_NAME = "moexIssFxSpotHandler";

    @Bean(BEAN_NAME)
    public MoexIssFxSpotHandler moexIssFxSpotHandler(
            @Qualifier(MoexIssFxSpotWebClientConfig.BEAN_NAME) WebClient webClient
    ) {
        return new MoexIssFxSpotHandler(webClient);
    }
}
