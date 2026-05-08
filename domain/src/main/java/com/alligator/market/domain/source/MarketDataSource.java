package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.policy.MarketDataSourcePolicy;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import org.reactivestreams.Publisher;

public interface MarketDataSource {

    MarketDataSourceCode sourceCode();

    MarketDataSourcePassport passport();

    MarketDataSourcePolicy policy();

    <I extends Instrument> Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
