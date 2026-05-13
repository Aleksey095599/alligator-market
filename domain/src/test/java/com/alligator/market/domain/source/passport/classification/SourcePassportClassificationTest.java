package com.alligator.market.domain.source.passport.classification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SourcePassportClassificationTest {
    @Test
    void deliveryModeCodesMustFitDomainLimit() {
        for (DeliveryMode mode : DeliveryMode.values()) {
            assertTrue(
                    mode.code().length() <= DeliveryMode.MAX_CODE_LENGTH,
                    () -> "DeliveryMode code exceeds " + DeliveryMode.MAX_CODE_LENGTH + " characters: " + mode.code()
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
