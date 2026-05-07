package com.alligator.market.backend.sourceplan.config.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.adapter.JooqInstrumentExistenceAdapter;
import com.alligator.market.backend.sourceplan.plan.application.port.InstrumentExistencePort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link InstrumentExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class InstrumentExistencePortWiringConfig {

    public static final String BEAN_INSTRUMENT_EXISTENCE_PORT = "instrumentExistencePort";

    @Bean(BEAN_INSTRUMENT_EXISTENCE_PORT)
    public InstrumentExistencePort instrumentExistencePort(DSLContext dsl) {
        return new JooqInstrumentExistenceAdapter(dsl);
    }
}
