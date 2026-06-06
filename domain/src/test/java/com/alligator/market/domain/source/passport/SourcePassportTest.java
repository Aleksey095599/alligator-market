package com.alligator.market.domain.source.passport;

import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SourcePassportTest {

    @Test
    void normalizesDescription() {
        SourcePassport passport = new SourcePassport(
                SourceDisplayName.of("Test Source"),
                " Test source description "
        );

        assertEquals("Test source description", passport.description());
    }

    @Test
    void rejectsInvalidDescription() {
        SourceDisplayName displayName = SourceDisplayName.of("Test Source");

        assertThrows(
                IllegalArgumentException.class,
                () -> new SourcePassport(displayName, " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SourcePassport(displayName, "a".repeat(101))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SourcePassport(displayName, "bad\ntext")
        );
    }
}
