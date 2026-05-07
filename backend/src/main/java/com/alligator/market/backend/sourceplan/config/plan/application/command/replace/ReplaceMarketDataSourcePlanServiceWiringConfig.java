package com.alligator.market.backend.sourceplan.config.plan.application.command.replace;

import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataCaptureProcessExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.InstrumentExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.ProviderExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.InstrumentExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.ProviderExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.command.replace.ReplaceMarketDataSourcePlanService;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link ReplaceMarketDataSourcePlanService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePlanRepositoryWiringConfig.class,
        MarketDataCaptureProcessExistencePortWiringConfig.class,
        InstrumentExistencePortWiringConfig.class,
        ProviderExistencePortWiringConfig.class
})
public class ReplaceMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_SERVICE = "replaceMarketDataSourcePlanService";

    @Bean(BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public ReplaceMarketDataSourcePlanService replaceMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            @Qualifier(MarketDataCaptureProcessExistencePortWiringConfig.BEAN_CAPTURE_PROCESS_EXISTENCE_PORT)
            MarketDataCaptureProcessExistencePort captureProcessExistencePort,
            @Qualifier(InstrumentExistencePortWiringConfig.BEAN_INSTRUMENT_EXISTENCE_PORT)
            InstrumentExistencePort instrumentExistencePort,
            @Qualifier(ProviderExistencePortWiringConfig.BEAN_PROVIDER_EXISTENCE_PORT)
            ProviderExistencePort providerExistencePort
    ) {
        return new ReplaceMarketDataSourcePlanService(
                marketDataSourcePlanRepository,
                new MarketDataSourcePlanValidator(
                        captureProcessExistencePort,
                        instrumentExistencePort,
                        providerExistencePort
                )
        );
    }
}
