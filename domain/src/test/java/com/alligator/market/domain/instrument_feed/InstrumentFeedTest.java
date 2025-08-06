package com.alligator.market.domain.instrument_feed;

import com.alligator.market.domain.instrument.currency.Currency;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.model.InstrumentHandler;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.quote.instrument_feed.InstrumentFeed;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест сортировки источников по приоритету.
 */
class InstrumentFeedTest {

    /* Проверяем корректный порядок при сортировке. */
    @Test
    void feedsSortedByPriority() {
        Currency instrument = new Currency("USD", "Dollar", "USA", 2);

        InstrumentFeed f1 = new InstrumentFeed(instrument, provider("P1"), 1);
        InstrumentFeed f2 = new InstrumentFeed(instrument, provider("P2"), 3);
        InstrumentFeed f3 = new InstrumentFeed(instrument, provider("P3"), 2);

        List<InstrumentFeed> feeds = new ArrayList<>(List.of(f2, f3, f1));

        // Сортировка по приоритету
        Collections.sort(feeds);

        assertEquals(List.of(f1, f3, f2), feeds);
    }

    /* Создаем простого провайдера. */
    private static MarketDataProvider provider(String code) {
        ProviderProfile profile = new ProviderProfile(
                code,
                code,
                Set.of(InstrumentType.CURRENCY),
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                0
        );

        // Возвращаем провайдера с пустыми обработчиками
        return new MarketDataProvider() {
            @Override public ProviderProfile profile() { return profile; }
            @Override public Map<InstrumentType, InstrumentHandler> instrumentHandlers() { return Map.of(); }
        };
    }
}

