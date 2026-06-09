package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;
import org.reactivestreams.Publisher;

import java.util.Set;

public interface MarketDataSource {

    SourceCode code();

    SourcePassport passport();

    Set<? extends InstrumentHandler<? extends MarketDataSource, ? extends Instrument>> handlers();

    <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument);
}
