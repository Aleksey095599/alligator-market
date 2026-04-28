package com.alligator.market.backend.provider.config.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.backend.provider.config.adapter.moex.iss.instrument.forex.spot.web.MoexIssFxSpotWebClientConfig;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Wiring-конфигурация {@link MoexIssFxSpotHandler}.
 */
@Configuration(proxyBeanMethods = false)
@Import(MoexIssFxSpotWebClientConfig.class)
public class MoexIssFxSpotHandlerConfig {

    public static final String BEAN_NAME = "moexIssFxSpotHandler";

    /**
     * Бин {@link MoexIssFxSpotHandler}.
     *
     * @param webClient web-клиент обработчика финансового инструмента FOREX_SPOT провайдера MOEX ISS
     */
    @Bean(BEAN_NAME)
    public MoexIssFxSpotHandler moexIssFxSpotHandler(
            @Qualifier(MoexIssFxSpotWebClientConfig.BEAN_NAME) WebClient webClient
    ) {
        return new MoexIssFxSpotHandler(webClient);
    }
}
