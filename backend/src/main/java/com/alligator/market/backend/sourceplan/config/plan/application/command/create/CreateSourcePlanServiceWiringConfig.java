package com.alligator.market.backend.sourceplan.config.plan.application.command.create;

import com.alligator.market.backend.instrument.config.registry.InstrumentRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataCapturerExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataSourceExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.SourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.common.SourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.command.create.CreateSourcePlanService;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
import com.alligator.market.domain.instrument.registry.InstrumentRegistry;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePlanRepositoryWiringConfig.class,
        MarketDataCapturerExistencePortWiringConfig.class,
        InstrumentRegistryWiringConfig.class,
        MarketDataSourceExistencePortWiringConfig.class
})
public class CreateSourcePlanServiceWiringConfig {
    public static final String BEAN_CREATE_SOURCE_PLAN_SERVICE = "createSourcePlanService";

    @Bean(BEAN_CREATE_SOURCE_PLAN_SERVICE)
    public CreateSourcePlanService createSourcePlanService(
            @Qualifier(SourcePlanRepositoryWiringConfig.BEAN_SOURCE_PLAN_REPOSITORY)
            SourcePlanRepository sourcePlanRepository,
            @Qualifier(MarketDataCapturerExistencePortWiringConfig.BEAN_CAPTURER_EXISTENCE_PORT)
            MarketDataCapturerExistencePort capturerExistencePort,
            @Qualifier(InstrumentRegistryWiringConfig.BEAN_INSTRUMENT_REGISTRY)
            InstrumentRegistry instrumentRegistry,
            @Qualifier(MarketDataSourceExistencePortWiringConfig.BEAN_MARKET_DATA_SOURCE_EXISTENCE_PORT)
            MarketDataSourceExistencePort sourceExistencePort
    ) {
        return new CreateSourcePlanService(
                sourcePlanRepository,
                new SourcePlanValidator(
                        capturerExistencePort,
                        instrumentRegistry,
                        sourceExistencePort
                )
        );
    }
}
