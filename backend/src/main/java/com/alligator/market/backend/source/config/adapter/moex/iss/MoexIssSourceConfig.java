package com.alligator.market.backend.source.config.adapter.moex.iss;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssSource;
import com.alligator.market.backend.source.config.adapter.moex.iss.handlers.MoexIssHandlersConfig;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration(proxyBeanMethods = false)
@Import(MoexIssHandlersConfig.class)
public class MoexIssSourceConfig {
    public static final String BEAN_NAME = "moexIssSource";

    @Bean(BEAN_NAME)
    public MoexIssSource moexIssSource(
            @Qualifier(MoexIssHandlersConfig.BEAN_NAME)
            Set<InstrumentHandler<MoexIssSource, ? extends Instrument>> handlers
    ) {
        return new MoexIssSource(handlers);
    }
}
