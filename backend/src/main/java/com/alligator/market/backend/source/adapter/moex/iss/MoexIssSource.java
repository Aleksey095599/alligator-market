package com.alligator.market.backend.source.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.source.passport.classification.AccessMethod;
import com.alligator.market.domain.source.passport.classification.DeliveryMode;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.policy.SourcePolicy;

import java.time.Duration;
import java.util.Set;

public final class MoexIssSource extends AbstractMarketSource<MoexIssSource> {
    public static final String SOURCE_CODE_VALUE = "MOEX_ISS";
    public static final SourceCode SOURCE_CODE =
            SourceCode.of(SOURCE_CODE_VALUE);

    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    private static final SourcePassport PASSPORT = new SourcePassport(
            SourceDisplayName.of(DISPLAY_NAME),
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    private static final Duration MIN_UPDATE_INTERVAL = Duration.ofSeconds(1);

    private static final SourcePolicy POLICY = new SourcePolicy(
            MIN_UPDATE_INTERVAL
    );

    public MoexIssSource(
            Set<? extends InstrumentHandler<MoexIssSource, ? extends Instrument>> handlers
    ) {
        super(SOURCE_CODE, PASSPORT, POLICY, handlers);
    }

    @Override
    protected MoexIssSource self() {
        return this;
    }
}
