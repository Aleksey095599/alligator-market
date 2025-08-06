package com.alligator.market.domain.instrument_feed;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.model.MarketDataProvider;

/**
 * Связка инструмента и провайдера с приоритетом обработки.
 *
 * @param instrument инструмент, данные по которому будут запрашиваться
 * @param provider   провайдер рыночных данных
 * @param priority   порядок обработки, чем меньше значение — тем выше приоритет
 */
public record InstrumentFeed(

        Instrument instrument,
        MarketDataProvider provider,
        int priority

) implements Comparable<InstrumentFeed> {

    @Override
    public int compareTo(InstrumentFeed other) {
        // Сортируем по возрастанию приоритета
        return Integer.compare(this.priority, other.priority);
    }
}

