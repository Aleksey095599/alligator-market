package com.alligator.market.backend.sourcing.config.plan.application.query.list;

import com.alligator.market.backend.sourcing.config.plan.repository.adapter.InstrumentSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.query.list.ListInstrumentSourcePlansService;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring {@link ListInstrumentSourcePlansService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanRepositoryWiringConfig.class
})
public class ListInstrumentSourcePlansServiceWiringConfig {

    public static final String BEAN_LIST_INSTRUMENT_SOURCE_PLANS_SERVICE = "listInstrumentSourcePlansService";

    /**
     * Сервис чтения планов источников рыночных данных.
     */
    @Bean(BEAN_LIST_INSTRUMENT_SOURCE_PLANS_SERVICE)
    public ListInstrumentSourcePlansService listInstrumentSourcePlansService(
            @Qualifier(InstrumentSourcePlanRepositoryWiringConfig.BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
            InstrumentSourcePlanRepository instrumentSourcePlanRepository
    ) {
        return new ListInstrumentSourcePlansService(instrumentSourcePlanRepository);
    }
}
