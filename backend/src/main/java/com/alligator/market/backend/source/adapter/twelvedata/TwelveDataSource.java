package com.alligator.market.backend.source.adapter.twelvedata;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;

import java.util.Set;

public final class TwelveDataSource extends AbstractMarketSource<TwelveDataSource> {
    public TwelveDataSource(
            Set<? extends InstrumentHandler<TwelveDataSource, ? extends Instrument>> handlers
    ) {
        super(TwelveDataSourcePassport.SOURCE_CODE, TwelveDataSourcePassport.INSTANCE, handlers);
    }

    @Override
    protected TwelveDataSource self() {
        return this;
    }
}
