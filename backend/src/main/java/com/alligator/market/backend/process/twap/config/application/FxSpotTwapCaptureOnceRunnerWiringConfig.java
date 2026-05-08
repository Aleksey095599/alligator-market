package com.alligator.market.backend.process.twap.config.application;

import com.alligator.market.backend.process.twap.capturer.FxSpotTwapCapturer;
import com.alligator.market.backend.process.twap.config.capturer.FxSpotTwapCapturerWiringConfig;
import com.alligator.market.backend.process.twap.application.FxSpotTwapCaptureOnceService;
import com.alligator.market.backend.process.twap.application.runner.FxSpotTwapCaptureOnceRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile("dev-capture-once")
@Import({
        FxSpotTwapCapturerWiringConfig.class,
        FxSpotTwapCaptureOnceServiceWiringConfig.class
})
public class FxSpotTwapCaptureOnceRunnerWiringConfig {
    public static final String BEAN_FX_SPOT_TWAP_CAPTURE_ONCE_RUNNER =
            "fxSpotTwapCaptureOnceRunner";

    @Bean(BEAN_FX_SPOT_TWAP_CAPTURE_ONCE_RUNNER)
    public ApplicationRunner fxSpotTwapCaptureOnceRunner(
            FxSpotTwapCaptureOnceService service,
            @Qualifier(FxSpotTwapCapturerWiringConfig.BEAN_FX_SPOT_TWAP_CAPTURER)
            FxSpotTwapCapturer capturer
    ) {
        return new FxSpotTwapCaptureOnceRunner(service, capturer);
    }
}
