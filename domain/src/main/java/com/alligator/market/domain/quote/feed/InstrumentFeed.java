package com.alligator.market.domain.quote.feed;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.MarketDataProvider;

/**
 * Связка инструмента и провайдера.
 *
 * @param instrument финансовый инструмент {@link Instrument}
 * @param provider   провайдер рыночных данных {@link MarketDataProvider}
 * @param priority   приоритет провайдера (1,2,3...)
 */
public record InstrumentFeed(

        Instrument instrument,
        MarketDataProvider provider,
        int priority
) {}

