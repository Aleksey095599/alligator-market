package com.alligator.market.backend.source.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;

import java.util.Set;

public final class MoexIssSource extends AbstractMarketSource<MoexIssSource> {
    public MoexIssSource(
            Set<? extends InstrumentHandler<MoexIssSource, ? extends Instrument>> handlers
    ) {
        super(MoexIssSourcePassport.SOURCE_CODE, MoexIssSourcePassport.INSTANCE, handlers);
    }

    @Override
    protected MoexIssSource self() {
        return this;
    }
}
