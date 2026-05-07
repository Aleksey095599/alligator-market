package com.alligator.market.backend.provider.config.adapter.moex.iss;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssMarketDataSource;
import com.alligator.market.backend.provider.config.adapter.moex.iss.handlers.MoexIssHandlersConfig;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

/**
 * Wiring-конфигурация {@link MoexIssMarketDataSource}.
 */
@Configuration(proxyBeanMethods = false)
@Import(MoexIssHandlersConfig.class)
public class MoexIssMarketDataSourceConfig {

    public static final String BEAN_NAME = "moexIssMarketDataSource";

    /**
     * Бин {@link MoexIssMarketDataSource}.
     *
     * @param handlers набор обработчиков инструментов source MOEX ISS
     */
    @Bean(BEAN_NAME)
    public MoexIssMarketDataSource moexIssMarketDataSource(
            @Qualifier(MoexIssHandlersConfig.BEAN_NAME)
            Set<InstrumentHandler<MoexIssMarketDataSource, ? extends Instrument>> handlers
    ) {
        return new MoexIssMarketDataSource(handlers);
    }
}
