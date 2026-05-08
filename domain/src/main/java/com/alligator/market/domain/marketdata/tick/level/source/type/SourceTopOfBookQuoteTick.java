package com.alligator.market.domain.marketdata.tick.level.source.type;

import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Source-level top-of-book quote tick with best bid and ask prices.
 */
public record SourceTopOfBookQuoteTick(
        SourceInstrumentCode sourceInstrumentCode,
        BigDecimal bidPrice,
        BigDecimal askPrice,
        Instant sourceTimestamp
) implements SourceMarketDataTick {

    public SourceTopOfBookQuoteTick {
        Objects.requireNonNull(sourceInstrumentCode, "sourceInstrumentCode must not be null");
        Objects.requireNonNull(bidPrice, "bidPrice must not be null");
        Objects.requireNonNull(askPrice, "askPrice must not be null");
        Objects.requireNonNull(sourceTimestamp, "sourceTimestamp must not be null");

        if (bidPrice.signum() <= 0) {
            throw new IllegalArgumentException("bidPrice must be positive");
        }

        if (askPrice.signum() <= 0) {
            throw new IllegalArgumentException("askPrice must be positive");
        }

        if (bidPrice.compareTo(askPrice) > 0) {
            throw new IllegalArgumentException("bidPrice must not be greater than askPrice");
        }
    }

    @Override
    public SourceMarketDataTickType sourceTickType() {
        return SourceMarketDataTickType.TOP_OF_BOOK_QUOTE;
    }
}
