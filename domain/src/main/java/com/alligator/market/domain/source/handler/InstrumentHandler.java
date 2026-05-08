package com.alligator.market.domain.source.handler;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Internal SPI for an instrument handler attached to a market data source.
 *
 * <p>The generic parameters bind a handler to a concrete source type and instrument type.</p>
 */
public interface InstrumentHandler<P extends MarketDataSource, I extends Instrument> {

    /**
     * Unique handler code.
     */
    HandlerCode handlerCode();

    /**
     * Supported instrument codes.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Attaches the handler to a market data source.
     */
    void attachTo(P source);

    /**
     * Streams source-level market data ticks for the given instrument.
     */
    Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
