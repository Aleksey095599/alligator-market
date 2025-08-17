package com.alligator.market.backend.instrument.type.forex.outright.catalog.service;

import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.FxOutrightNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import com.alligator.market.domain.instrument.type.forex.outright.model.ValueDateCode;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для {@link FxOutrightServiceImpl}.
 */
class FxOutrightServiceImplTest {

    /** Простейшее in-memory хранилище. */
    static class InMemoryFxOutrightStorage implements FxOutrightStorage {
        private final Map<String, FxOutright> map = new HashMap<>();

        @Override
        public void save(FxOutright instrument) {
            map.put(instrument.code(), instrument);
        }

        @Override
        public void delete(String code) {
            map.remove(code);
        }

        @Override
        public Optional<FxOutright> find(String code) {
            return Optional.ofNullable(map.get(code));
        }

        @Override
        public List<FxOutright> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public boolean existsByCurrency(String currencyCode) {
            return map.values().stream()
                    .anyMatch(i -> i.baseCurrency().equals(currencyCode) || i.quoteCurrency().equals(currencyCode));
        }
    }

    @Test
    void shouldUpdateQuoteDecimalOnly() {
        InMemoryFxOutrightStorage storage = new InMemoryFxOutrightStorage();
        FxOutrightServiceImpl service = new FxOutrightServiceImpl(storage);
        FxOutright original = new FxOutright("EUR", "USD", ValueDateCode.TOM, 4);
        storage.save(original);

        service.updateQuoteDecimal(original.code(), 6);

        FxOutright updated = storage.find(original.code()).orElseThrow();
        assertEquals(6, updated.quoteDecimal());
        assertEquals("EUR", updated.baseCurrency());
        assertEquals("USD", updated.quoteCurrency());
        assertEquals(ValueDateCode.TOM, updated.valueDateCode());
    }

    @Test
    void shouldThrowWhenUpdatingAbsent() {
        InMemoryFxOutrightStorage storage = new InMemoryFxOutrightStorage();
        FxOutrightServiceImpl service = new FxOutrightServiceImpl(storage);
        assertThrows(FxOutrightNotFoundException.class,
                () -> service.updateQuoteDecimal("AAA", 1));
    }
}

