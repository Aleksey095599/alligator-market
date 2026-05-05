package com.alligator.market.backend.marketdata.config.capture.process;

import com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.fxspot.analytical.lastprice.application.AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.startup.MarketDataCaptureProcessPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.passport.application.query.list.MarketDataCaptureProcessPassportListServiceWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.registry.MarketDataCaptureProcessRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи marketdata.capture.process.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AnalyticalFxSpotTwapLastPriceWiringConfig.class,
        AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig.class,
        MarketDataCaptureProcessRegistryWiringConfig.class,
        MarketDataCaptureProcessPassportListServiceWiringConfig.class,
        MarketDataCaptureProcessPassportProjectionStartupRunnerWiringConfig.class
})
public class MarketDataCaptureProcessFeatureWiringConfig {
}
