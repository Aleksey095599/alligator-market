package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;
import org.reactivestreams.Publisher;

public interface MarketDataSource {

    SourceCode code();

    SourcePassport passport();

    <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument);
}
