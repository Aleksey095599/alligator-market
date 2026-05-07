package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.policy.MarketDataSourcePolicy;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import org.reactivestreams.Publisher;

/**
 * Domain contract of a runtime market data source.
 */
public interface MarketDataSource {

    /**
     * Unique source code.
     */
    MarketDataSourceCode sourceCode();

    /**
     * Source passport.
     */
    MarketDataSourcePassport passport();

    /**
     * Source policy.
     */
    MarketDataSourcePolicy policy();

    /**
     * Streams source-level market data ticks for the given instrument.
     */
    <I extends Instrument> Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
