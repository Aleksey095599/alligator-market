package com.alligator.market.backend.provider.adapter.moex.iss.config;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.config.handler.instrument.forex.spot.MoexIssFxSpotHandlerConfig;
import com.alligator.market.backend.provider.adapter.moex.iss.config.registry.MoexIssHandlersRegistryConfig;
import com.alligator.market.backend.provider.adapter.moex.iss.config.web.MoexIssWebClientConfig;
import com.alligator.market.backend.provider.adapter.moex.iss.properties.MoexIssConnectionProperties;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.model.handler.AbstractInstrumentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

/**
 * Entry-point конфигурация бинов провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MoexIssConnectionProperties.class)
@Import({
        MoexIssWebClientConfig.class,
        MoexIssFxSpotHandlerConfig.class,
        MoexIssHandlersRegistryConfig.class
})
public class MoexIssProviderConfig {

    /* Бин провайдера MOEX ISS. */
    @Bean
    public MoexIssProvider moexIssProvider(
            @Qualifier("moexIssHandlers")
            Set<AbstractInstrumentHandler<MoexIssProvider, ? extends Instrument>> handlers
    ) {
        return new MoexIssProvider(handlers);
    }
}
