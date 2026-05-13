package com.alligator.market.domain.shared.vo;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;

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
}
