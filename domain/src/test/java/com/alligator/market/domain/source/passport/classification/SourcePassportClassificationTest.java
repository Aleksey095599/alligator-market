package com.alligator.market.domain.source.passport.classification;

import com.alligator.market.domain.source.passport.AccessMethod;
import com.alligator.market.domain.source.passport.DeliveryMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SourcePassportClassificationTest {
    @Test
    void deliveryModeCodesMustHaveDomainLength() {
        for (DeliveryMode mode : DeliveryMode.values()) {
            assertTrue(
                    mode.code().length() == DeliveryMode.CODE_LENGTH,
                    () -> "DeliveryMode code must be exactly " + DeliveryMode.CODE_LENGTH + " characters: " + mode.code()
            );
        }
    }

    @Test
    void accessMethodCodesMustFitDomainLimit() {
        for (AccessMethod method : AccessMethod.values()) {
            assertTrue(
                    method.code().length() <= AccessMethod.MAX_CODE_LENGTH,
                    () -> "AccessMethod code exceeds " + AccessMethod.MAX_CODE_LENGTH + " characters: " + method.code()
            );
        }
    }
}
