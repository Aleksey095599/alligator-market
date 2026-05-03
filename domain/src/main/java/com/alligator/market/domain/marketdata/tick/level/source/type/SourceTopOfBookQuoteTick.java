package com.alligator.market.domain.marketdata.tick.level.source.type;

import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.classification.SourceMarketDataTickType;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Source-level рыночный тик типа "Top-of-book Quote" с лучшими ценами bid/ask на время предоставления тика.
 *
 * @param sourceInstrumentCode идентификатор инструмента в системе источника
 * @param bidPrice             лучшая цена bid
 * @param askPrice             лучшая цена ask
 * @param sourceTimestamp      время предоставления цен источником
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
