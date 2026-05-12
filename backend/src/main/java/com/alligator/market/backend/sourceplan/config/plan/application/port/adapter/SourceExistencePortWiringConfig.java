package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.SourceExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqSourceExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourceExistencePortWiringConfig {
    public static final String BEAN_SOURCE_EXISTENCE_PORT = "sourceExistencePort";

    @Bean(BEAN_SOURCE_EXISTENCE_PORT)
    public SourceExistencePort sourceExistencePort(DSLContext dsl) {
        return new JooqSourceExistenceAdapter(dsl);
    }
}
