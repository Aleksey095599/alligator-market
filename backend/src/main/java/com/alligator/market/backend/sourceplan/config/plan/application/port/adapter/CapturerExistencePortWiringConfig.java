package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.CapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqCapturerExistenceAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CapturerExistencePortWiringConfig {
    public static final String BEAN_CAPTURER_EXISTENCE_PORT = "capturerExistencePort";

    @Bean(BEAN_CAPTURER_EXISTENCE_PORT)
    public CapturerExistencePort capturerExistencePort(DSLContext dsl) {
        return new JooqCapturerExistenceAdapter(dsl);
    }
}
