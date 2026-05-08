package com.alligator.market.backend.process.twap.config.application;

import com.alligator.market.backend.process.twap.application.FxSpotTwapCaptureOnceService;
import com.alligator.market.backend.process.twap.application.runner.FxSpotTwapCaptureOnceRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Development-only wiring for one-shot FX Spot TWAP capture.
 */
@Configuration(proxyBeanMethods = false)
@Profile("dev-capture-once")
@Import({
        FxSpotTwapCaptureOnceServiceWiringConfig.class
})
public class FxSpotTwapCaptureOnceRunnerWiringConfig {

    public static final String BEAN_FX_SPOT_TWAP_CAPTURE_ONCE_RUNNER =
            "fxSpotTwapCaptureOnceRunner";

    @Bean(BEAN_FX_SPOT_TWAP_CAPTURE_ONCE_RUNNER)
    public ApplicationRunner fxSpotTwapCaptureOnceRunner(
            FxSpotTwapCaptureOnceService service
    ) {
        return new FxSpotTwapCaptureOnceRunner(service);
    }
}
