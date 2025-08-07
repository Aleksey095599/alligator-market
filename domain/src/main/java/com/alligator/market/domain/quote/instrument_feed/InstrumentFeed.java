package com.alligator.market.domain.quote.instrument_feed;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.MarketDataProvider;

import java.util.Objects;

/**
 * Связка инструмента и провайдера.
 *
 * @param instrument финансовый инструмент {@link Instrument}
 * @param provider   провайдер рыночных данных {@link MarketDataProvider}
 * @param priority   приоритет провайдера (самый высокий =1)
 */
public record InstrumentFeed(

        Instrument instrument,
        MarketDataProvider provider,
        int priority
) {}

