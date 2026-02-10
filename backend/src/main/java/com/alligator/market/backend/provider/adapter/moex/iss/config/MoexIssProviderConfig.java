package com.alligator.market.backend.provider.adapter.moex.iss.config;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.config.handlers.MoexIssHandlersConfig;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.model.handler.AbstractInstrumentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

/**
 * Конфигурация бина провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
@Import(MoexIssHandlersConfig.class)
public class MoexIssProviderConfig {

    /* Бин провайдера MOEX ISS. */
    @Bean
    public MoexIssProvider moexIssProvider(
            @Qualifier(MoexIssHandlersConfig.BEAN_HANDLERS)
            Set<AbstractInstrumentHandler<MoexIssProvider, ? extends Instrument>> handlers
    ) {
        return new MoexIssProvider(handlers);
    }
}
