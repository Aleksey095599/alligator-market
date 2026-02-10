package com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.forex.spot.web;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.properties.MoexIssFxSpotConnectionProperties;
import com.alligator.market.backend.provider.config.web.GlobalProviderWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация web-клиента обработчика финансового инструмента FX_SPOT провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
@Import(GlobalProviderWebClientConfig.class)
@EnableConfigurationProperties(MoexIssFxSpotConnectionProperties.class)
public class MoexIssFxSpotWebClientConfig {

    public static final String BEAN_WEB_CLIENT = "moexIssFxSpotWebClient";

    /**
     * Бин web-клиента обработчика финансового инструмента FX_SPOT провайдера MOEX ISS.
     *
     * @param baseWebClient базовый web-клиент для всех провайдеров
     * @param props параметры подключения к провайдеру MOEX ISS по инструментам типа FX_SPOT
     */
    @Bean(BEAN_WEB_CLIENT)
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
