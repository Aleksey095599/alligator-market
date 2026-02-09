package com.alligator.market.backend.provider.adapter.moex.iss.config.handler.instrument.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.handler.instrument.forex.spot.MoexIssFxSpotHandler;
import com.alligator.market.backend.provider.adapter.moex.iss.properties.MoexIssConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация бинов обработчика инструментов FX_SPOT провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
public class MoexIssFxSpotHandlerConfig {

    /**
     * Бин обработчика FX_SPOT для MOEX ISS.
     *
     * <p>Бин инжектирует параметры подключения и web-клиент для выполнения запросов к провайдеру.</p>
     */
    @Bean
    public MoexIssFxSpotHandler moexIssFxSpotHandler(
            MoexIssConnectionProperties connectionProps,
            @Qualifier("moexIssWebClient") WebClient webClient
    ) {
        return new MoexIssFxSpotHandler(connectionProps, webClient);
    }
}
