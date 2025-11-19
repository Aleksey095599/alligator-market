package com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/* Юнит-тесты для строгого парсера чисел toDecimal(...) в ProFinanceFxSpotHandler. */
class ProFinanceFxSpotHandlerToDecimalTest {

    /* Вспомогательный метод: вызывает private static toDecimal(String) через reflection. */
    private static BigDecimal invokeToDecimal(String raw) {
        try {
            Method m = ProFinanceFxSpotHandler.class.getDeclaredMethod("toDecimal", String.class);
            m.setAccessible(true);
            return (BigDecimal) m.invoke(null, raw);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Reflection invoke failed", e);
        }
    }

    // ------------------------------- Позитивные кейсы -------------------------------

    @Test
    void parsesInteger() {
        BigDecimal v = invokeToDecimal("1234");
        assertEquals(new BigDecimal("1234"), v);
    }

    @Test
    void parsesDecimalWithDot() {
        BigDecimal v = invokeToDecimal("1.2345");
        assertEquals(new BigDecimal("1.2345"), v);
    }

    @Test
    void parsesWithNbspAsThousands() {
        String s = "1\u00A0234.56"; // NBSP между 1 и 234
        BigDecimal v = invokeToDecimal(s);
        assertEquals(new BigDecimal("1234.56"), v);
    }

    @Test
    void parsesWithNarrowNbspAndThinSpace() {
        String s = "1\u202F234\u2009.56"; // узкий NBSP и тонкий пробел
        BigDecimal v = invokeToDecimal(s);
        assertEquals(new BigDecimal("1234.56"), v);
    }

    // ------------------------------- Негативные кейсы -------------------------------

    @Test
    void rejectsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeToDecimal(null));
        assertTrue(ex.getMessage().contains("Input value for decimal parsing is null"));
    }

    @Test
    void rejectsComma() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> invokeToDecimal("1,234.56"));
        assertTrue(ex.getMessage().contains("Decimal comma is not allowed"));
    }

    @Test
    void rejectsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> invokeToDecimal("-1.00"));
        assertTrue(ex.getMessage().contains("Negative value is not allowed"));
    }

    @Test
    void rejectsUnicodeMinus() {
        String s = "−1.23"; // U+2212
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeToDecimal(s));
        assertTrue(ex.getMessage().contains("Negative value is not allowed"));
    }

    @Test
    void rejectsTwoDots() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> invokeToDecimal("1.2.3"));
        assertTrue(ex.getMessage().contains("More than one dot"));
    }

    @Test
    void rejectsAlpha() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> invokeToDecimal("abc"));
        assertTrue(ex.getMessage().contains("Invalid numeric format"));
    }

    @Test
    void rejectsLeadingDot() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> invokeToDecimal(".123"));
        assertTrue(ex.getMessage().contains("Invalid numeric format"));
    }

    @Test
    void rejectsTrailingDot() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> invokeToDecimal("123."));
        assertTrue(ex.getMessage().contains("Invalid numeric format"));
    }
}
