package com.alligator.market.backend.sourcing.config.plan.application.command.create;

import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.MarketDataCaptureProcessExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.InstrumentExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.ProviderExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateMarketDataSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderExistencePort;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
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
        MarketDataCaptureProcessExistencePortWiringConfig.class,
        InstrumentExistencePortWiringConfig.class,
        ProviderExistencePortWiringConfig.class
})
public class CreateMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_CREATE_MARKET_DATA_SOURCE_PLAN_SERVICE = "createMarketDataSourcePlanService";

    @Bean(BEAN_CREATE_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public CreateMarketDataSourcePlanService createMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            @Qualifier(MarketDataCaptureProcessExistencePortWiringConfig.BEAN_CAPTURE_PROCESS_EXISTENCE_PORT)
            MarketDataCaptureProcessExistencePort captureProcessExistencePort,
            @Qualifier(InstrumentExistencePortWiringConfig.BEAN_INSTRUMENT_EXISTENCE_PORT)
            InstrumentExistencePort instrumentExistencePort,
            @Qualifier(ProviderExistencePortWiringConfig.BEAN_PROVIDER_EXISTENCE_PORT)
            ProviderExistencePort providerExistencePort
    ) {
        return new CreateMarketDataSourcePlanService(
                marketDataSourcePlanRepository,
                new MarketDataSourcePlanValidator(
                        captureProcessExistencePort,
                        instrumentExistencePort,
                        providerExistencePort
                )
        );
    }
}
