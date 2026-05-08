package com.alligator.market.backend.capturer.config;

import com.alligator.market.backend.capturer.config.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceCapturerWiringConfig;
import com.alligator.market.backend.capturer.config.catalog.twap.fxspot.analytical.lastprice.application.AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig;
import com.alligator.market.backend.capturer.config.passport.application.projection.startup.MarketDataCapturerPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.capturer.config.passport.application.query.list.MarketDataCapturerPassportListServiceWiringConfig;
import com.alligator.market.backend.capturer.config.registry.MarketDataCapturerRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи capturer.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AnalyticalFxSpotTwapLastPriceCapturerWiringConfig.class,
        AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig.class,
        MarketDataCapturerRegistryWiringConfig.class,
        MarketDataCapturerPassportListServiceWiringConfig.class,
        MarketDataCapturerPassportProjectionStartupRunnerWiringConfig.class
})
public class MarketDataCapturerFeatureWiringConfig {
}
