package com.alligator.market.backend.config.capturer;

import com.alligator.market.backend.config.capturer.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceCapturerWiringConfig;
import com.alligator.market.backend.config.capturer.catalog.twap.fxspot.analytical.lastprice.application.AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig;
import com.alligator.market.backend.config.capturer.passport.application.projection.startup.MarketDataCapturerPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.config.capturer.passport.application.query.list.MarketDataCapturerPassportListServiceWiringConfig;
import com.alligator.market.backend.config.capturer.registry.MarketDataCapturerRegistryWiringConfig;
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
