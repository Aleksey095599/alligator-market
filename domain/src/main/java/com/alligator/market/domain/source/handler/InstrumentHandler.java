package com.alligator.market.domain.source.handler;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.Set;

public interface InstrumentHandler<P extends MarketSource, I extends Instrument> {

    HandlerCode handlerCode();

    Set<InstrumentCode> supportedInstrumentCodes();

    void attachTo(P source);

    Publisher<SourceTick> streamSourceTicks(I instrument);
}
