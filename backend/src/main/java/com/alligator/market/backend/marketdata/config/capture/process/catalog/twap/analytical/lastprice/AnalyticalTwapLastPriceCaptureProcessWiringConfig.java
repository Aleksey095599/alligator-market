package com.alligator.market.backend.marketdata.config.capture.process.catalog.twap.analytical.lastprice;

import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.support.MoexIssFxSpotSupportCatalog;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.catalog.twap.analytical.lastprice.AnalyticalTwapLastPriceCaptureProcess;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wiring-конфигурация {@link AnalyticalTwapLastPriceCaptureProcess}.
 */
@Configuration(proxyBeanMethods = false)
public class AnalyticalTwapLastPriceCaptureProcessWiringConfig {

    public static final String BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_PROCESS =
            "analyticalTwapLastPriceCaptureProcess";

    /**
     * Доменный процесс фиксации тиков последней цены для аналитического TWAP.
     */
    @Bean(BEAN_ANALYTICAL_TWAP_LAST_PRICE_CAPTURE_PROCESS)
    public AnalyticalTwapLastPriceCaptureProcess analyticalTwapLastPriceCaptureProcess() {
        return new AnalyticalTwapLastPriceCaptureProcess(supportedInstrumentCodes());
    }

    private static Set<InstrumentCode> supportedInstrumentCodes() {
        return MoexIssFxSpotSupportCatalog.SUPPORTED_INSTRUMENTS.stream()
                .map(FxSpot::instrumentCode)
                .collect(Collectors.toUnmodifiableSet());
    }
}
