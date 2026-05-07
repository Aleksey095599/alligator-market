package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.policy.ProviderPolicy;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.reactivestreams.Publisher;

/**
 * Domain contract of a runtime market data source.
 */
public interface MarketDataSource {

    /**
     * Unique source code.
     */
    ProviderCode providerCode();

    /**
     * Source passport.
     */
    ProviderPassport passport();

    /**
     * Source policy.
     */
    ProviderPolicy policy();

    /**
     * Streams source-level market data ticks for the given instrument.
     */
    <I extends Instrument> Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
