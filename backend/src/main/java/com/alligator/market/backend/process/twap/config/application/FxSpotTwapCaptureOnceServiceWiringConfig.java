package com.alligator.market.backend.process.twap.config.application;

import com.alligator.market.backend.infra.time.config.TimeWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.backend.process.twap.application.FxSpotTwapCaptureOnceService;
import com.alligator.market.backend.process.twap.config.persistence.jooq.repository.FxSpotTwapCapturedTicksRepositoryWiringConfig;
import com.alligator.market.backend.source.config.registry.MarketDataSourceRegistryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.SourcePlanRepositoryWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.process.twap.repository.FxSpotTwapCapturedTicksRepository;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * Wiring-конфигурация {@link FxSpotTwapCaptureOnceService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        SourcePlanRepositoryWiringConfig.class,
        MarketDataSourceRegistryWiringConfig.class,
        FxSpotRepositoryWiringConfig.class,
        FxSpotTwapCapturedTicksRepositoryWiringConfig.class,
        TimeWiringConfig.class
})
public class FxSpotTwapCaptureOnceServiceWiringConfig {

    public static final String BEAN_FX_SPOT_TWAP_CAPTURE_ONCE_SERVICE =
            "fxSpotTwapCaptureOnceService";

    @Bean(BEAN_FX_SPOT_TWAP_CAPTURE_ONCE_SERVICE)
    public FxSpotTwapCaptureOnceService fxSpotTwapCaptureOnceService(
            @Qualifier(SourcePlanRepositoryWiringConfig.BEAN_SOURCE_PLAN_REPOSITORY)
            SourcePlanRepository sourcePlanRepository,
            @Qualifier(MarketDataSourceRegistryWiringConfig.BEAN_MARKET_DATA_SOURCE_REGISTRY)
            MarketDataSourceRegistry sourceRegistry,
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository,
            @Qualifier(FxSpotTwapCapturedTicksRepositoryWiringConfig.BEAN_FX_SPOT_TWAP_CAPTURED_TICKS_REPOSITORY)
            FxSpotTwapCapturedTicksRepository capturedTickRepository,
            Clock clock
    ) {
        return new FxSpotTwapCaptureOnceService(
                sourcePlanRepository,
                sourceRegistry,
                fxSpotRepository,
                capturedTickRepository,
                clock
        );
    }
}
