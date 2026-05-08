package com.alligator.market.backend.capturer.config;

import com.alligator.market.backend.process.twap.config.application.FxSpotTwapCaptureOnceServiceWiringConfig;
import com.alligator.market.backend.process.twap.config.application.FxSpotTwapCaptureOnceRunnerWiringConfig;
import com.alligator.market.backend.capturer.config.passport.application.projection.startup.MarketDataCapturerPassportProjectionStartupRunnerWiringConfig;
import com.alligator.market.backend.capturer.config.passport.application.query.list.MarketDataCapturerPassportListServiceWiringConfig;
import com.alligator.market.backend.capturer.config.registry.MarketDataCapturerRegistryWiringConfig;
import com.alligator.market.backend.process.twap.config.capturer.FxSpotTwapCapturerWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        FxSpotTwapCapturerWiringConfig.class,
        FxSpotTwapCaptureOnceServiceWiringConfig.class,
        FxSpotTwapCaptureOnceRunnerWiringConfig.class,
        MarketDataCapturerRegistryWiringConfig.class,
        MarketDataCapturerPassportListServiceWiringConfig.class,
        MarketDataCapturerPassportProjectionStartupRunnerWiringConfig.class
})
public class MarketDataCapturerFeatureWiringConfig {
}
