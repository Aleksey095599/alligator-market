package com.alligator.market.backend.sourcing.config.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.adapter.JooqInstrumentCodeExistenceAdapter;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring {@link InstrumentCodeExistencePort}.
 */
@Configuration(proxyBeanMethods = false)
public class InstrumentCodeExistencePortWiringConfig {

    public static final String BEAN_INSTRUMENT_CODE_EXISTENCE_PORT = "instrumentCodeExistencePort";

    /**
     * Порт проверки существования инструмента по коду.
     */
    @Bean(BEAN_INSTRUMENT_CODE_EXISTENCE_PORT)
    public InstrumentCodeExistencePort instrumentCodeExistencePort(DSLContext dsl) {
        return new JooqInstrumentCodeExistenceAdapter(dsl);
    }
}
