package com.alligator.market.domain.quote.feed.model;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;

/**
 * Модель связки инструмента и провайдера рыночных данных.
 *
 * @param instrument финансовый инструмент {@link Instrument}
 * @param provider   провайдер рыночных данных {@link MarketDataProvider}
 * @param priority   приоритет провайдера 1 (наивысший), 2, 3...
 */
public record InstrumentFeed(

        Instrument instrument,
        MarketDataProvider provider,
        int priority
) {}

