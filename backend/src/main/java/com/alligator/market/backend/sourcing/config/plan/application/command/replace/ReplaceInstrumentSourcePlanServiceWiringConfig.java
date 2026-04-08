package com.alligator.market.backend.sourcing.config.plan.application.command.replace;

import com.alligator.market.backend.sourcing.config.plan.application.command.create.port.InstrumentCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.create.port.ProviderCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.repository.InstrumentSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.command.create.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.command.create.port.ProviderCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceInstrumentSourcePlanService;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring {@link ReplaceInstrumentSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanRepositoryWiringConfig.class,
        InstrumentCodeExistencePortWiringConfig.class,
        ProviderCodeExistencePortWiringConfig.class
})
public class ReplaceInstrumentSourcePlanServiceWiringConfig {

    public static final String BEAN_REPLACE_INSTRUMENT_SOURCE_PLAN_SERVICE = "replaceInstrumentSourcePlanService";

    /**
     * Сервис полной замены плана источников рыночных данных для инструмента.
     */
    @Bean(BEAN_REPLACE_INSTRUMENT_SOURCE_PLAN_SERVICE)
    public ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService(
            @Qualifier(InstrumentSourcePlanRepositoryWiringConfig.BEAN_INSTRUMENT_SOURCE_PLAN_REPOSITORY)
            InstrumentSourcePlanRepository instrumentSourcePlanRepository,
            @Qualifier(InstrumentCodeExistencePortWiringConfig.BEAN_INSTRUMENT_CODE_EXISTENCE_PORT)
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            @Qualifier(ProviderCodeExistencePortWiringConfig.BEAN_PROVIDER_CODE_EXISTENCE_PORT)
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
        return new ReplaceInstrumentSourcePlanService(
                instrumentSourcePlanRepository,
                instrumentCodeExistencePort,
                providerCodeExistencePort
        );
    }
}
