package com.alligator.market.backend.marketdata.config.capture.application.port;

import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.backend.marketdata.capture.application.port.MarketDataCaptureInstrumentResolver;
import com.alligator.market.backend.marketdata.capture.application.port.adapter.FxSpotMarketDataCaptureInstrumentResolverAdapter;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link MarketDataCaptureInstrumentResolver}.
 */
@Configuration(proxyBeanMethods = false)
@Import(FxSpotRepositoryWiringConfig.class)
public class MarketDataCaptureInstrumentResolverWiringConfig {

    public static final String BEAN_MARKET_DATA_CAPTURE_INSTRUMENT_RESOLVER =
            "marketDataCaptureInstrumentResolver";

    @Bean(BEAN_MARKET_DATA_CAPTURE_INSTRUMENT_RESOLVER)
    public MarketDataCaptureInstrumentResolver marketDataCaptureInstrumentResolver(
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository
    ) {
        return new FxSpotMarketDataCaptureInstrumentResolverAdapter(fxSpotRepository);
    }
}
