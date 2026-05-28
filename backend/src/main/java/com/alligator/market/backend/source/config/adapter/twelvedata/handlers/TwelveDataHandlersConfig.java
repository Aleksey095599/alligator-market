package com.alligator.market.backend.source.config.adapter.twelvedata.handlers;

import com.alligator.market.backend.source.adapter.twelvedata.TwelveDataMarketDataSource;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandler;
import com.alligator.market.backend.source.config.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandlerConfig;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration(proxyBeanMethods = false)
@Import({
        TwelveDataFxSpotHandlerConfig.class
})
public class TwelveDataHandlersConfig {
    public static final String BEAN_NAME = "twelveDataHandlers";

    @Bean(BEAN_NAME)
    public Set<InstrumentHandler<TwelveDataMarketDataSource, ? extends Instrument>> twelveDataHandlers(
            TwelveDataFxSpotHandler fxSpotHandler
    ) {
        return Set.of(fxSpotHandler);
    }
}
