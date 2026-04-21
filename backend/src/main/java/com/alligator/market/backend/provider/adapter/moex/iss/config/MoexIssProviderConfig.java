package com.alligator.market.backend.provider.adapter.moex.iss.config;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.config.handlers.MoexIssHandlersConfig;
import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.handler.ProviderManagedInstrumentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

/**
 * Wiring-конфигурация {@link MoexIssProvider}.
 */
@Configuration(proxyBeanMethods = false)
@Import(MoexIssHandlersConfig.class)
public class MoexIssProviderConfig {

    public static final String BEAN_NAME = "moexIssProvider";

    /**
     * Бин {@link MoexIssProvider}.
     *
     * @param handlers набор обработчиков инструментов провайдера MOEX ISS
     */
    @Bean(BEAN_NAME)
    public MoexIssProvider moexIssProvider(
            @Qualifier(MoexIssHandlersConfig.BEAN_NAME)
            Set<ProviderManagedInstrumentHandler<MoexIssProvider, ? extends Instrument>> handlers
    ) {
        return new MoexIssProvider(handlers);
    }
}
