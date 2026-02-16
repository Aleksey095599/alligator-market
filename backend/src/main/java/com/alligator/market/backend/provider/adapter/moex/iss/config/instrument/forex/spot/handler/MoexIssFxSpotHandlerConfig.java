package com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.forex.spot.handler;

import com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.forex.spot.web.MoexIssFxSpotWebClientConfig;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация wiring {@link MoexIssFxSpotHandler}.
 */
@Configuration(proxyBeanMethods = false)
@Import(MoexIssFxSpotWebClientConfig.class)
public class MoexIssFxSpotHandlerConfig {

    /**
     * Бин обработчика инструментов типа FX_SPOT провайдера MOEX ISS.
     *
     * @param webClient web-клиент для выполнения запросов к провайдеру
     */
    @Bean
    public MoexIssFxSpotHandler moexIssFxSpotHandler(
            @Qualifier(MoexIssFxSpotWebClientConfig.BEAN_WEB_CLIENT) WebClient webClient
    ) {
        return new MoexIssFxSpotHandler(webClient);
    }
}
