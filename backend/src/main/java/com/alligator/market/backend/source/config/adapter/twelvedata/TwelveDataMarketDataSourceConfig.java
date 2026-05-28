package com.alligator.market.backend.source.config.adapter.twelvedata;

import com.alligator.market.backend.source.adapter.twelvedata.TwelveDataMarketDataSource;
import com.alligator.market.backend.source.config.adapter.twelvedata.handlers.TwelveDataHandlersConfig;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration(proxyBeanMethods = false)
@Import(TwelveDataHandlersConfig.class)
public class TwelveDataMarketDataSourceConfig {
    public static final String BEAN_NAME = "twelveDataMarketDataSource";

    @Bean(BEAN_NAME)
    public TwelveDataMarketDataSource twelveDataMarketDataSource(
            @Qualifier(TwelveDataHandlersConfig.BEAN_NAME)
            Set<InstrumentHandler<TwelveDataMarketDataSource, ? extends Instrument>> handlers
    ) {
        return new TwelveDataMarketDataSource(handlers);
    }
}
