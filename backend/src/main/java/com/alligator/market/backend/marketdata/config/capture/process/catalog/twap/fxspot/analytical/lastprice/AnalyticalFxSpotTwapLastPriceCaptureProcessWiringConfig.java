package com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceCaptureProcess;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link AnalyticalFxSpotTwapLastPriceCaptureProcess}.
 */
@Configuration(proxyBeanMethods = false)
public class AnalyticalFxSpotTwapLastPriceCaptureProcessWiringConfig {

    public static final String BEAN_ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE_CAPTURE_PROCESS =
            "analyticalFxSpotTwapLastPriceCaptureProcess";

    /**
     * Backend-описание процесса захвата, регистрируемое в capture process registry.
     */
    @Bean(BEAN_ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE_CAPTURE_PROCESS)
    public AnalyticalFxSpotTwapLastPriceCaptureProcess analyticalFxSpotTwapLastPriceCaptureProcess() {
        return new AnalyticalFxSpotTwapLastPriceCaptureProcess();
    }
}
