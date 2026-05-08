package com.alligator.market.backend.process.twap.config.capturer;

import com.alligator.market.backend.process.twap.capturer.FxSpotTwapCapturer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring configuration for {@link FxSpotTwapCapturer}.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotTwapCapturerWiringConfig {

    public static final String BEAN_FX_SPOT_TWAP_CAPTURER =
            "fxSpotTwapCapturer";

    @Bean(BEAN_FX_SPOT_TWAP_CAPTURER)
    public FxSpotTwapCapturer fxSpotTwapCapturer() {
        return new FxSpotTwapCapturer();
    }
}
