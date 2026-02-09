package com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.type.forex.spot.handler;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.type.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.type.forex.spot.properties.MoexIssFxSpotConnectionProperties;
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
            MoexIssFxSpotConnectionProperties connectionProps,
            @Qualifier("moexIssWebClient") WebClient webClient
    ) {
        return new MoexIssFxSpotHandler(connectionProps, webClient);
    }
}
