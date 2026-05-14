package com.alligator.market.domain.shared.vo;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainCodeLengthTest {
    private static final int STANDARD_CODE_LENGTH = 50;

    @Test
    void capturerCodeAcceptsStandardLength() {
        assertDoesNotThrow(() -> CapturerCode.of("A".repeat(STANDARD_CODE_LENGTH)));
    }

    @Test
    void capturerCodeRejectsLongerThanStandardLength() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CapturerCode.of("A".repeat(STANDARD_CODE_LENGTH + 1))
        );
    }

    @Test
    void capturerCodeRejectsNonDomainCodeCharacters() {
        assertThrows(IllegalArgumentException.class, () -> CapturerCode.of("CAPTURER-1"));
    }

    @Test
    void sourceCodeAcceptsStandardLength() {
        assertDoesNotThrow(() -> SourceCode.of("A".repeat(STANDARD_CODE_LENGTH)));
    }

    @Test
    void sourceCodeRejectsLongerThanStandardLength() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SourceCode.of("A".repeat(STANDARD_CODE_LENGTH + 1))
        );
    }

    @Test
    void sourceCodeRejectsNonDomainCodeCharacters() {
        assertThrows(IllegalArgumentException.class, () -> SourceCode.of("SOURCE.1"));
    }

    @Test
    void sourceInstrumentCodeUsesDomainCodeFormat() {
        assertEquals("MOEX_SECID_1", SourceInstrumentCode.of("moex_secid_1").value());
        assertThrows(IllegalArgumentException.class, () -> SourceInstrumentCode.of("MOEX-SECID"));
    }
}
