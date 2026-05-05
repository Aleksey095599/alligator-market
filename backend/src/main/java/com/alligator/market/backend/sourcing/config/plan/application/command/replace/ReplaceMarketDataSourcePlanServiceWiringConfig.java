package com.alligator.market.backend.sourcing.config.plan.application.command.replace;

import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.MDCaptureProcessCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.InstrumentCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.port.adapter.ProviderCodeExistencePortWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourcing.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourcing.plan.application.port.MDCaptureProcessCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceMarketDataSourcePlanService;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
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
        MDCaptureProcessCodeExistencePortWiringConfig.class,
        InstrumentCodeExistencePortWiringConfig.class,
        ProviderCodeExistencePortWiringConfig.class
})
public class ReplaceMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_SERVICE = "replaceMarketDataSourcePlanService";

    @Bean(BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public ReplaceMarketDataSourcePlanService replaceMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            @Qualifier(MDCaptureProcessCodeExistencePortWiringConfig.BEAN_CAPTURE_PROCESS_CODE_EXISTENCE_PORT)
            MDCaptureProcessCodeExistencePort captureProcessCodeExistencePort,
            @Qualifier(InstrumentCodeExistencePortWiringConfig.BEAN_INSTRUMENT_CODE_EXISTENCE_PORT)
            InstrumentCodeExistencePort instrumentCodeExistencePort,
            @Qualifier(ProviderCodeExistencePortWiringConfig.BEAN_PROVIDER_CODE_EXISTENCE_PORT)
            ProviderCodeExistencePort providerCodeExistencePort
    ) {
        return new ReplaceMarketDataSourcePlanService(
                marketDataSourcePlanRepository,
                new MarketDataSourcePlanValidator(
                        captureProcessCodeExistencePort,
                        instrumentCodeExistencePort,
                        providerCodeExistencePort
                )
        );
    }
}
