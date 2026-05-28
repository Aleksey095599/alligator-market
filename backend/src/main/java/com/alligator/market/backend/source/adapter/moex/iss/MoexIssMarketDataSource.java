package com.alligator.market.backend.source.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;

import java.util.Set;

public final class MoexIssMarketDataSource extends AbstractMarketDataSource<MoexIssMarketDataSource> {
    public MoexIssMarketDataSource(
            Set<? extends InstrumentHandler<MoexIssMarketDataSource, ? extends Instrument>> handlers
    ) {
        super(MoexIssMarketDataSourcePassport.SOURCE_CODE, MoexIssMarketDataSourcePassport.INSTANCE, handlers);
    }

    @Override
    protected MoexIssMarketDataSource self() {
        return this;
    }
}
