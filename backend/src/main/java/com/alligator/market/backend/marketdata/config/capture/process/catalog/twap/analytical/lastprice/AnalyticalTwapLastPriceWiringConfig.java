package com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.analytical.lastprice;

import com.alligator.market.domain.marketdata.capture.process.catalog.twap.analytical.lastprice.AnalyticalTwapLastPrice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link AnalyticalTwapLastPrice}.
 */
@Configuration(proxyBeanMethods = false)
public class AnalyticalTwapLastPriceWiringConfig {

    public static final String BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_PROCESS =
            "analyticalTwapLastPriceCaptureProcess";

    /**
     * Доменный процесс фиксации тиков последней цены для аналитического TWAP.
     */
    @Bean(BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_PROCESS)
    public AnalyticalTwapLastPrice analyticalTwapLastPriceCaptureProcess() {
        return new AnalyticalTwapLastPrice();
    }
}
