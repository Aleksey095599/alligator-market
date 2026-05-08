package com.alligator.market.domain.marketdata.tick.level.source.type;

import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record SourceLastPriceTick(
        SourceInstrumentCode sourceInstrumentCode,
        BigDecimal lastPrice,
        Instant sourceTimestamp
) implements SourceMarketDataTick {

    public SourceLastPriceTick {
        Objects.requireNonNull(sourceInstrumentCode, "sourceInstrumentCode must not be null");
        Objects.requireNonNull(lastPrice, "lastPrice must not be null");
        Objects.requireNonNull(sourceTimestamp, "sourceTimestamp must not be null");

        if (lastPrice.signum() <= 0) {
            throw new IllegalArgumentException("lastPrice must be positive");
        }
    }

    @Override
    public SourceMarketDataTickType sourceTickType() {
        return SourceMarketDataTickType.LAST_PRICE;
    }
}
