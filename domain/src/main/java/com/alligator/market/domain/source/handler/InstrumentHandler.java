package com.alligator.market.domain.source.handler;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;
import com.alligator.market.domain.source.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.Set;

public interface InstrumentHandler<P extends MarketDataSource, I extends Instrument> {

    HandlerCode handlerCode();

    SourceHandlerPassport passport();

    Set<InstrumentCode> supportedInstrumentCodes();

    SourceHandlerPolicy policy();

    void attachTo(P source);

    Publisher<SourceTick> streamSourceTicks(I instrument);
}
