package com.alligator.market.backend.sourceplan.config.plan.application.command.create;

import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataCaptureProcessExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.InstrumentExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataSourceExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.command.create.CreateMarketDataSourcePlanService;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.InstrumentExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
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
        MarketDataSourceExistencePortWiringConfig.class
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
            @Qualifier(MarketDataSourceExistencePortWiringConfig.BEAN_MARKET_DATA_SOURCE_EXISTENCE_PORT)
            MarketDataSourceExistencePort sourceExistencePort
    ) {
        return new CreateMarketDataSourcePlanService(
                marketDataSourcePlanRepository,
                new MarketDataSourcePlanValidator(
                        captureProcessExistencePort,
                        instrumentExistencePort,
                        sourceExistencePort
                )
        );
    }
}
