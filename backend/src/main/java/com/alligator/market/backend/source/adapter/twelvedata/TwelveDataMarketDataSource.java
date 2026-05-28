package com.alligator.market.backend.source.adapter.twelvedata;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;

import java.util.Set;

public final class TwelveDataMarketDataSource extends AbstractMarketDataSource<TwelveDataMarketDataSource> {
    public TwelveDataMarketDataSource(
            Set<? extends InstrumentHandler<TwelveDataMarketDataSource, ? extends Instrument>> handlers
    ) {
        super(TwelveDataMarketDataSourcePassport.SOURCE_CODE, TwelveDataMarketDataSourcePassport.INSTANCE, handlers);
    }

    @Override
    protected TwelveDataMarketDataSource self() {
        return this;
    }
}
