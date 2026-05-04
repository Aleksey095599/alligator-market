package com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.analytical.lastprice.application;

import com.alligator.market.backend.infra.time.config.TimeWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.AnalyticalTwapLastPriceCaptureOnceService;
import com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.analytical.lastprice.AnalyticalTwapLastPriceWiringConfig;
import com.alligator.market.backend.marketdata.config.tick.persistence.jooq.repository.CapturedMarketDataTickRepositoryWiringConfig;
import com.alligator.market.backend.provider.config.registry.ProviderRegistryWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.persistence.jooq.repository.MarketDataSourcePlanRepositoryWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.marketdata.capture.process.catalog.twap.analytical.lastprice.AnalyticalTwapLastPrice;
import com.alligator.market.domain.marketdata.tick.level.capture.repository.CapturedMarketDataTickRepository;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * Wiring-конфигурация {@link AnalyticalTwapLastPriceCaptureOnceService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AnalyticalTwapLastPriceWiringConfig.class,
        MarketDataSourcePlanRepositoryWiringConfig.class,
        ProviderRegistryWiringConfig.class,
        FxSpotRepositoryWiringConfig.class,
        CapturedMarketDataTickRepositoryWiringConfig.class,
        TimeWiringConfig.class
})
public class AnalyticalTwapLastPriceCaptureOnceServiceWiringConfig {

    public static final String BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_ONCE_SERVICE =
            "analyticalTwapLastPriceCaptureOnceService";

    @Bean(BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_ONCE_SERVICE)
    public AnalyticalTwapLastPriceCaptureOnceService analyticalTwapLastPriceCaptureOnceService(
            @Qualifier(AnalyticalTwapLastPriceWiringConfig.BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_PROCESS)
            AnalyticalTwapLastPrice process,
            @Qualifier(MarketDataSourcePlanRepositoryWiringConfig.BEAN_MARKET_DATA_SOURCE_PLAN_REPOSITORY)
            MarketDataSourcePlanRepository sourcePlanRepository,
            @Qualifier(ProviderRegistryWiringConfig.BEAN_PROVIDER_REGISTRY)
            ProviderRegistry providerRegistry,
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository,
            @Qualifier(CapturedMarketDataTickRepositoryWiringConfig.BEAN_CAPTURED_MARKET_DATA_TICK_REPOSITORY)
            CapturedMarketDataTickRepository capturedTickRepository,
            Clock clock
    ) {
        return new AnalyticalTwapLastPriceCaptureOnceService(
                process,
                sourcePlanRepository,
                providerRegistry,
                fxSpotRepository,
                capturedTickRepository,
                clock
        );
    }
}
