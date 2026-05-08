package com.alligator.market.backend.sourceplan.config.plan.application.command.replace;

import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataCapturerExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.InstrumentExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.port.adapter.MarketDataSourceExistencePortWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCapturerExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.InstrumentExistencePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
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
        MarketDataCapturerExistencePortWiringConfig.class,
        InstrumentExistencePortWiringConfig.class,
        MarketDataSourceExistencePortWiringConfig.class
})
public class ReplaceMarketDataSourcePlanServiceWiringConfig {

    public static final String BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_SERVICE = "replaceMarketDataSourcePlanService";

    @Bean(BEAN_REPLACE_MARKET_DATA_SOURCE_PLAN_SERVICE)
    public ReplaceMarketDataSourcePlanService replaceMarketDataSourcePlanService(
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            @Qualifier(MarketDataCapturerExistencePortWiringConfig.BEAN_CAPTURER_EXISTENCE_PORT)
            MarketDataCapturerExistencePort capturerExistencePort,
            @Qualifier(InstrumentExistencePortWiringConfig.BEAN_INSTRUMENT_EXISTENCE_PORT)
            InstrumentExistencePort instrumentExistencePort,
            @Qualifier(MarketDataSourceExistencePortWiringConfig.BEAN_MARKET_DATA_SOURCE_EXISTENCE_PORT)
            MarketDataSourceExistencePort sourceExistencePort
    ) {
        return new ReplaceMarketDataSourcePlanService(
                marketDataSourcePlanRepository,
                new MarketDataSourcePlanValidator(
                        capturerExistencePort,
                        instrumentExistencePort,
                        sourceExistencePort
                )
        );
    }
}
