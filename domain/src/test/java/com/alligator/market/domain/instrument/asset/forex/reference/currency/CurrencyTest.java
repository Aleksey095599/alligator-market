package com.alligator.market.domain.instrument.asset.forex.reference.currency;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyTest {

    @Test
    void normalizesHumanReadableFields() {
        Currency currency = new Currency(
                CurrencyCode.of("USD"),
                " United States Dollar ",
                " United States ",
                2
        );

        assertEquals("United States Dollar", currency.name());
        assertEquals("United States", currency.country());
    }

    @Test
    void rejectsTooLongHumanReadableFields() {
        String tooLong = "A".repeat(51);

        assertThrows(IllegalArgumentException.class, () ->
                new Currency(CurrencyCode.of("USD"), tooLong, "United States", 2)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Currency(CurrencyCode.of("USD"), "United States Dollar", tooLong, 2)
        );
    }

    @Test
    void rejectsControlCharactersInHumanReadableFields() {
        assertThrows(IllegalArgumentException.class, () ->
                new Currency(CurrencyCode.of("USD"), "United\nStates Dollar", "United States", 2)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Currency(CurrencyCode.of("USD"), "United States Dollar", "United\nStates", 2)
        );
    }
}
