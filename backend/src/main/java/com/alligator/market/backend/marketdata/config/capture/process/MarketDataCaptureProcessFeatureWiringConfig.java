package com.alligator.market.backend.marketdata.config.capture.process;

import com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.analytical.lastprice.AnalyticalTwapLastPriceCaptureProcessWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.registry.CaptureProcessRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи marketdata.capture.process.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AnalyticalTwapLastPriceCaptureProcessWiringConfig.class,
        CaptureProcessRegistryWiringConfig.class
})
public class MarketDataCaptureProcessFeatureWiringConfig {
}
