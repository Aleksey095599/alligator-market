package com.alligator.market.backend.config.capturer.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.backend.capturer.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceCapturer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link AnalyticalFxSpotTwapLastPriceCapturer}.
 */
@Configuration(proxyBeanMethods = false)
public class AnalyticalFxSpotTwapLastPriceCapturerWiringConfig {

    public static final String BEAN_ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE_CAPTURER =
            "analyticalFxSpotTwapLastPriceCapturer";

    /**
     * Backend-описание процесса захвата, регистрируемое в capturer registry.
     */
    @Bean(BEAN_ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE_CAPTURER)
    public AnalyticalFxSpotTwapLastPriceCapturer analyticalFxSpotTwapLastPriceCapturer() {
        return new AnalyticalFxSpotTwapLastPriceCapturer();
    }
}
