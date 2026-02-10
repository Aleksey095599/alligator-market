package com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.type.forex.spot.web;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.type.forex.spot.properties.MoexIssFxSpotConnectionProperties;
import com.alligator.market.backend.provider.config.web.GlobalProviderWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация web-клиента обработчика финансового инструмента FX_SPOT (провайдер MOEX ISS).
 */
@Configuration(proxyBeanMethods = false)
@Import(GlobalProviderWebClientConfig.class)
public class MoexIssFxSpotWebClientConfig {

    /**
     * Бин web-клиента обработчика финансового инструмента FX_SPOT (провайдер MOEX ISS).
     *
     * @param baseWebClient базовый web-клиент для всех провайдеров
     * @param props параметры подключения к провайдеру MOEX ISS для инструментов FX_SPOT
     */
    @Bean("moexIssFxSpotWebClient")
    public WebClient moexIssFxSpotWebClient(
            @Qualifier(GlobalProviderWebClientConfig.BEAN_BASE_WEB_CLIENT) WebClient baseWebClient,
            MoexIssFxSpotConnectionProperties props
    ) {
        return baseWebClient.mutate()
                .baseUrl(props.baseUrl())
                // + специфичные настройки обработчика
                .build();
    }
}
