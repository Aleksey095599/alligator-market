package com.alligator.market.backend.sourcing.config.plan.application.command.create;

import com.alligator.market.backend.sourcing.config.plan.application.command.create.port.InstrumentCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.create.port.ProviderCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.repository.InstrumentSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.command.create.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.command.create.port.ProviderCodeExistencePort;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring {@link CreateInstrumentSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanRepositoryWiringConfig.class,
        InstrumentCodeExistencePortWiringConfig.class,
        ProviderCodeExistencePortWiringConfig.class
})
public class CreateInstrumentSourcePlanServiceWiringConfig {

    public static final String BEAN_CREATE_INSTRUMENT_SOURCE_PLAN_SERVICE = "createInstrumentSourcePlanService";

    /**
     * Сервис создания плана источников рыночных данных для инструмента.
     */
    @Bean(BEAN_CREATE_INSTRUMENT_SOURCE_PLAN_SERVICE)
    public CreateInstrumentSourcePlanService createInstrumentSourcePlanService(
            @Qualifier(InstrumentSourcePlanRepositoryWiringConfig.BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
            InstrumentSourcePlanRepository instrumentSourcePlanRepository,
            @Qualifier(InstrumentCodeExistencePortWiringConfig.BEAN_INSTRUMENT_CODE_EXISTENCE_PORT)
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            @Qualifier(ProviderCodeExistencePortWiringConfig.BEAN_PROVIDER_CODE_EXISTENCE_PORT)
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
        return new CreateInstrumentSourcePlanService(
                instrumentSourcePlanRepository,
                instrumentCodeExistencePort,
                providerCodeExistencePort
        );
    }
}
