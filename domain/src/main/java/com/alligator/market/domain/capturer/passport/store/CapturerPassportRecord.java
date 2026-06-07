package com.alligator.market.domain.capturer.passport.store;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.shared.code.DomainCodeFormat;

import java.util.Objects;

public record CapturerPassportRecord(
        CapturerCode capturerCode,
        CapturerPassport passport,
        RegistryStatus registryStatus
) {

    public CapturerPassportRecord {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }

    public static CapturerPassportRecord registered(
            CapturerCode capturerCode,
            CapturerPassport passport
    ) {
        return new CapturerPassportRecord(capturerCode, passport, RegistryStatus.REGISTERED);
    }

    public static CapturerPassportRecord retired(
            CapturerCode capturerCode,
            CapturerPassport passport
    ) {
        return new CapturerPassportRecord(capturerCode, passport, RegistryStatus.RETIRED);
    }

    public enum RegistryStatus {
        REGISTERED,
        RETIRED;

        private static final int MAX_CODE_LENGTH = 10;

        RegistryStatus() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "CapturerPassportRecord.RegistryStatus code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("CapturerPassportRecord.RegistryStatus", name());
        }
    }
}
