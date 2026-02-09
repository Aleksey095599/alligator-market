package com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.type.forex.spot.web;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.type.forex.spot.properties.MoexIssFxSpotConnectionProperties;
import com.alligator.market.backend.provider.config.web.GlobalProviderWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация web-клиента провайдера рыночных данных MOEX ISS.
 *
 * <p>Использует единую для всех провайдеров конфигурацию базового слоя WebClient {@link GlobalProviderWebClientConfig}
 * и параметры подключения {@link MoexIssFxSpotConnectionProperties}.</p>
 */
@Configuration(proxyBeanMethods = false)
@Import(GlobalProviderWebClientConfig.class)
public class MoexIssFxSpotWebClientConfig {

    /* Параметры подключения к провайдеру MOEX ISS. */
    private final MoexIssFxSpotConnectionProperties connectionProps;

    /**
     * Бин Web-клиента провайдера рыночных данных MOEX ISS.
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
