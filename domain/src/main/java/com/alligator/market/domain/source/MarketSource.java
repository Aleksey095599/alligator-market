package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.policy.SourcePolicy;
import com.alligator.market.domain.source.vo.SourceCode;
import org.reactivestreams.Publisher;

public interface MarketSource {

    SourceCode sourceCode();

    SourcePassport passport();

    SourcePolicy policy();

    <I extends Instrument> Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
