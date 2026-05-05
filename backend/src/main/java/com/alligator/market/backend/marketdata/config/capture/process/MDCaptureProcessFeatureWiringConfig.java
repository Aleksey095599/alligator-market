package com.alligator.market.backend.marketdata.config.capture.process;

import com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.fxspot.analytical.lastprice.application.AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.passport.application.projection.startup.MDCaptureProcessPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.passport.application.query.list.MDCaptureProcessPassportListServiceWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.registry.MDCaptureProcessRegistryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи marketdata.capture.process.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AnalyticalFxSpotTwapLastPriceWiringConfig.class,
        AnalyticalFxSpotTwapLastPriceCaptureOnceServiceWiringConfig.class,
        MDCaptureProcessRegistryWiringConfig.class,
        MDCaptureProcessPassportListServiceWiringConfig.class,
        MDCaptureProcessPassportProjectionStartupRunnerWiringConfig.class
})
public class MDCaptureProcessFeatureWiringConfig {
}
