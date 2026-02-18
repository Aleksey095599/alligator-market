package com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.forex.spot.web;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.properties.MoexIssFxSpotConnectionProperties;
import com.alligator.market.backend.provider.config.web.BaseProviderWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация wiring web-клиента обработчика финансового инструмента FX_SPOT провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
@Import(BaseProviderWebClientConfig.class)
@EnableConfigurationProperties(MoexIssFxSpotConnectionProperties.class)
public class MoexIssFxSpotWebClientConfig {

    public static final String BEAN_NAME = "moexIssFxSpotWebClient";

    /**
     * Бин web-клиента обработчика финансового инструмента FX_SPOT провайдера MOEX ISS.
     *
     * @param globalWebClient единый для всех провайдеров web-клиент
     * @param props параметры подключения к провайдеру MOEX ISS по инструментам типа FX_SPOT
     */
    @Bean(BEAN_NAME)
    public WebClient moexIssFxSpotWebClient(
            @Qualifier(BaseProviderWebClientConfig.BEAN_NAME) WebClient globalWebClient,
            MoexIssFxSpotConnectionProperties props
    ) {
        return globalWebClient.mutate()
                .baseUrl(props.baseUrl())
                // + специфичные настройки обработчика
                .build();
    }
}
