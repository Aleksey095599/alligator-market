package com.alligator.market.backend.source.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;

import java.util.Set;

public final class MoexIssSource extends AbstractMarketSource<MoexIssSource> {
    public static final String SOURCE_CODE_VALUE = "MOEX_ISS";
    public static final SourceCode SOURCE_CODE =
            SourceCode.of(SOURCE_CODE_VALUE);

    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    private static final SourcePassport PASSPORT =
            new SourcePassport(SourceDisplayName.of(DISPLAY_NAME));

    public MoexIssSource(
            Set<? extends InstrumentHandler<MoexIssSource, ? extends Instrument>> handlers
    ) {
        super(SOURCE_CODE, PASSPORT, handlers);
    }

    @Override
    protected MoexIssSource self() {
        return this;
    }
}
