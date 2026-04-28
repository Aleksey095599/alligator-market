package com.alligator.market.backend.provider.config.adapter.moex.iss.instrument.forex.spot.web;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.properties.MoexIssFxSpotConnectionProperties;
import com.alligator.market.backend.provider.config.infra.handler.web.HandlerBaseWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Wiring-конфигурация web-клиента обработчика финансового инструмента FOREX_SPOT провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
@Import(HandlerBaseWebClientConfig.class)
@EnableConfigurationProperties(MoexIssFxSpotConnectionProperties.class)
public class MoexIssFxSpotWebClientConfig {

    public static final String BEAN_NAME = "moexIssFxSpotWebClient";

    /**
     * Бин web-клиента обработчика финансового инструмента FOREX_SPOT провайдера MOEX ISS.
     *
     * @param globalWebClient единый для всех провайдеров web-клиент
     * @param props параметры подключения к провайдеру MOEX ISS по инструментам типа FOREX_SPOT
     */
    @Bean(BEAN_NAME)
    public WebClient moexIssFxSpotWebClient(
            @Qualifier(HandlerBaseWebClientConfig.BEAN_HANDLER_BASE_WEB_CLIENT) WebClient globalWebClient,
            MoexIssFxSpotConnectionProperties props
    ) {
        return globalWebClient.mutate()
                .baseUrl(props.baseUrl())
                // + специфичные настройки обработчика
                .build();
    }
}
