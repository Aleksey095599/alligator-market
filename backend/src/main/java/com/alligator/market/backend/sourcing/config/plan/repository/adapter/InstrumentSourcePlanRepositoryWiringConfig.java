package com.alligator.market.backend.sourcing.config.plan.repository.adapter;

import com.alligator.market.backend.sourcing.plan.repository.adapter.JooqInstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring репозитория планов источников инструмента.
 */
@Configuration(proxyBeanMethods = false)
public class InstrumentSourcePlanRepositoryWiringConfig {

    public static final String BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY = "instrumentSourcePlanRepository";

    /**
     * Доменный порт репозитория планов источников через jOOQ-адаптер.
     */
    @Bean(BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
    public InstrumentSourcePlanRepository instrumentSourcePlanRepository(DSLContext dsl) {
        return new JooqInstrumentSourcePlanRepository(dsl);
    }
}
