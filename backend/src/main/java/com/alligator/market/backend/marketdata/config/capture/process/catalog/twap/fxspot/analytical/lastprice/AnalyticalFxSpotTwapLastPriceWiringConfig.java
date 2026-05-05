package com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.fxspot.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPrice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link AnalyticalFxSpotTwapLastPrice}.
 */
@Configuration(proxyBeanMethods = false)
public class AnalyticalFxSpotTwapLastPriceWiringConfig {

    public static final String BEAN_ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE_CAPTURE_PROCESS =
            "analyticalFxSpotTwapLastPriceMarketDataCaptureProcess";

    /**
     * Доменный процесс захвата FX Spot тиков последней цены для аналитического TWAP.
     */
    @Bean(BEAN_ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE_CAPTURE_PROCESS)
    public AnalyticalFxSpotTwapLastPrice analyticalFxSpotTwapLastPriceMarketDataCaptureProcess() {
        return new AnalyticalFxSpotTwapLastPrice();
    }
}
