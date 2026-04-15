package com.alligator.market.backend.sourcing.config.plan.application.query.get;

import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.InstrumentSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.query.get.GetInstrumentSourcePlanService;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link GetInstrumentSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanRepositoryWiringConfig.class
})
public class GetInstrumentSourcePlanServiceWiringConfig {

    public static final String BEAN_GET_INSTRUMENT_SOURCE_PLAN_SERVICE = "getInstrumentSourcePlanService";

    @Bean(BEAN_GET_INSTRUMENT_SOURCE_PLAN_SERVICE)
    public GetInstrumentSourcePlanService getInstrumentSourcePlanService(
            @Qualifier(InstrumentSourcePlanRepositoryWiringConfig.BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
            InstrumentSourcePlanRepository instrumentSourcePlanRepository
    ) {
        return new GetInstrumentSourcePlanService(instrumentSourcePlanRepository);
    }
}
