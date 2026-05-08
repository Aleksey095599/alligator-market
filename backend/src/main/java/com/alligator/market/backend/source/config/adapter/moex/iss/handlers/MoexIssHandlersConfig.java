package com.alligator.market.backend.source.config.adapter.moex.iss.handlers;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssMarketDataSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.backend.source.config.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandlerConfig;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration(proxyBeanMethods = false)
@Import({
        MoexIssFxSpotHandlerConfig.class

})
public class MoexIssHandlersConfig {
    public static final String BEAN_NAME = "moexIssHandlers";

    @Bean(BEAN_NAME)
    public Set<InstrumentHandler<MoexIssMarketDataSource, ? extends Instrument>> moexIssHandlers(
            MoexIssFxSpotHandler fxSpotHandler

    ) {
        return Set.of(fxSpotHandler);
    }
}
