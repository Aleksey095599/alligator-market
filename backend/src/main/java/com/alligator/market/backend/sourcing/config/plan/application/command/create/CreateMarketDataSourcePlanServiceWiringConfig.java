package com.alligator.market.backend.sourcing.config.plan.application.command.create;

import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.MarketDataCaptureProcessCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.InstrumentCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.ProviderCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateMarketDataSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link CreateMarketDataSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanRepositoryWiringConfig.class,
        MarketDataCaptureProcessCodeExistencePortWiringConfig.class,
        InstrumentCodeExistencePortWiringConfig.class,
        ProviderCodeExistencePortWiringConfig.class
})
public class CreateMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_CREATE_MARKET_DATA_SOURCE_PLAN_SERVICE = "createMarketDataSourcePlanService";

    @Bean(BEAN_CREATE_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public CreateMarketDataSourcePlanService createMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            @Qualifier(MarketDataCaptureProcessCodeExistencePortWiringConfig.BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT)
            MarketDataCaptureProcessCodeExistencePort captureProcessCodeExistencePort,
            @Qualifier(InstrumentCodeExistencePortWiringConfig.BEAN_INSTRUMENT_CODE_EXISTENCE_PORT)
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            @Qualifier(ProviderCodeExistencePortWiringConfig.BEAN_PROVIDER_CODE_EXISTENCE_PORT)
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
        return new CreateMarketDataSourcePlanService(
                marketDataSourcePlanRepository,
                new MarketDataSourcePlanValidator(
                        captureProcessCodeExistencePort,
                        instrumentCodeExistencePort,
                        providerCodeExistencePort
                )
        );
    }
}
