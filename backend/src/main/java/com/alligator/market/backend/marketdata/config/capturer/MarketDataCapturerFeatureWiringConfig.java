package com.alligator.market.backend.marketdata.config.capturer;

import com.alligator.market.backend.marketdata.config.capturer.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceCapturerWiringConfig;
import com.alligator.market.backend.marketdata.config.capturer.catalog.twap.fxspot.analytical.lastprice.application.AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig;
import com.alligator.market.backend.marketdata.config.capturer.passport.application.projection.startup.MarketDataCapturerPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.marketdata.config.capturer.passport.application.query.list.MarketDataCapturerPassportListServiceWiringConfig;
import com.alligator.market.backend.marketdata.config.capturer.registry.MarketDataCapturerRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи marketdata.capturer.
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
