package com.alligator.market.domain.marketdata.feed;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.MarketDataSource;

import java.util.Optional;

public interface CapturerFeed {

    Optional<MarketDataSource> sourceFor(MarketDataCapturer capturer, Instrument instrument);

    Optional<MarketDataSource> nextSourceAfter(
            MarketDataCapturer capturer,
            Instrument instrument,
            MarketDataSource currentSource
    );
}
