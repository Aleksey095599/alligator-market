package com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository;

import com.alligator.market.backend.sourcing.plan.persistence.jooq.repository.JooqInstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link InstrumentSourcePlanRepository}.
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
