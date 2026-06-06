package com.alligator.market.domain.capturer.passport;

import com.alligator.market.domain.capturer.vo.CapturerDisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapturerPassportTest {

    @Test
    void normalizesDescription() {
        CapturerPassport passport = new CapturerPassport(
                CapturerDisplayName.of("Test Capturer"),
                " Test capturer description "
        );

        assertEquals("Test capturer description", passport.description());
    }

    @Test
    void rejectsInvalidDescription() {
        CapturerDisplayName displayName = CapturerDisplayName.of("Test Capturer");

        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerPassport(displayName, " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerPassport(displayName, "a".repeat(101))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerPassport(displayName, "bad\ntext")
        );
    }
}
