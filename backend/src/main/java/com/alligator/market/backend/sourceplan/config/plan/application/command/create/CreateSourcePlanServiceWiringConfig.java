package com.alligator.market.backend.sourceplan.config.plan.application.command.create;

import com.alligator.market.backend.instrument.config.identity.InstrumentIdentityStoreWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.CapturerExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.SourceExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.SourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.registry.sync.RuntimeSourcePlanRegistryUpdaterWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.common.SourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.command.create.CreateSourcePlanService;
import com.alligator.market.backend.sourceplan.plan.application.port.CapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.SourceExistencePort;
import com.alligator.market.domain.instrument.identity.InstrumentIdentityStore;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePlanRepositoryWiringConfig.class,
        CapturerExistencePortWiringConfig.class,
        InstrumentIdentityStoreWiringConfig.class,
        SourceExistencePortWiringConfig.class,
        RuntimeSourcePlanRegistryUpdaterWiringConfig.class
})
public class CreateSourcePlanServiceWiringConfig {
    public static final String BEAN_CREATE_SOURCE_PLAN_SERVICE = "createSourcePlanService";

    @Bean(BEAN_CREATE_SOURCE_PLAN_SERVICE)
    public CreateSourcePlanService createSourcePlanService(
            @Qualifier(SourcePlanRepositoryWiringConfig.BEAN_SOURCE_PLAN_REPOSITORY)
            SourcePlanRepository sourcePlanRepository,
            @Qualifier(CapturerExistencePortWiringConfig.BEAN_CAPTURER_EXISTENCE_PORT)
            CapturerExistencePort capturerExistencePort,
            @Qualifier(InstrumentIdentityStoreWiringConfig.BEAN_INSTRUMENT_IDENTITY_STORE)
            InstrumentIdentityStore instrumentIdentityStore,
            @Qualifier(SourceExistencePortWiringConfig.BEAN_SOURCE_EXISTENCE_PORT)
            SourceExistencePort sourceExistencePort,
            @Qualifier(RuntimeSourcePlanRegistryUpdaterWiringConfig.BEAN_RUNTIME_SOURCE_PLAN_REGISTRY_UPDATER)
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater
    ) {
        return new CreateSourcePlanService(
                sourcePlanRepository,
                new SourcePlanValidator(
                        capturerExistencePort,
                        instrumentIdentityStore,
                        sourceExistencePort
                ),
                runtimeSourcePlanRegistryUpdater
        );
    }
}
