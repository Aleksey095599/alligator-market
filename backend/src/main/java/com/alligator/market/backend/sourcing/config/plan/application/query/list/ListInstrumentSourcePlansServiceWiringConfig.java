package com.alligator.market.backend.sourcing.config.plan.application.query.list;

import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.InstrumentSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.query.list.InstrumentSourcePlanListService;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link InstrumentSourcePlanListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanRepositoryWiringConfig.class
})
public class ListInstrumentSourcePlansServiceWiringConfig {

    public static final String BEAN_LIST_INSTRUMENT_SOURCE_PLANS_SERVICE = "listInstrumentSourcePlansService";

    @Bean(BEAN_LIST_INSTRUMENT_SOURCE_PLANS_SERVICE)
    public InstrumentSourcePlanListService listInstrumentSourcePlansService(
            @Qualifier(InstrumentSourcePlanRepositoryWiringConfig.BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
            InstrumentSourcePlanRepository instrumentSourcePlanRepository
    ) {
        return new InstrumentSourcePlanListService(instrumentSourcePlanRepository);
    }
}
