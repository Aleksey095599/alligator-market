package com.alligator.market.backend.sourcing.config.plan.application.command.delete;

import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.InstrumentSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.command.delete.DeleteInstrumentSourcePlanService;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link DeleteInstrumentSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanRepositoryWiringConfig.class
})
public class DeleteInstrumentSourcePlanServiceWiringConfig {

    public static final String BEAN_DELETE_INSTRUMENT_SOURCE_PLAN_SERVICE = "deleteInstrumentSourcePlanService";

    @Bean(BEAN_DELETE_INSTRUMENT_SOURCE_PLAN_SERVICE)
    public DeleteInstrumentSourcePlanService deleteInstrumentSourcePlanService(
            @Qualifier(InstrumentSourcePlanRepositoryWiringConfig.BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
            InstrumentSourcePlanRepository instrumentSourcePlanRepository
    ) {
        return new DeleteInstrumentSourcePlanService(instrumentSourcePlanRepository);
    }
}
