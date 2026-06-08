package com.alligator.market.domain.source.passport.store;

import com.alligator.market.domain.shared.code.DomainCodeFormat;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record SourcePassportRecord(
        SourceCode sourceCode,
        SourcePassport passport,
        RegistryStatus registryStatus
) {

    public SourcePassportRecord {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }

    public static SourcePassportRecord registered(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new SourcePassportRecord(sourceCode, passport, RegistryStatus.REGISTERED);
    }

    public static SourcePassportRecord retired(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new SourcePassportRecord(sourceCode, passport, RegistryStatus.RETIRED);
    }

    public enum RegistryStatus {
        REGISTERED,
        RETIRED;

        private static final int MAX_CODE_LENGTH = 10;

        RegistryStatus() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "SourcePassportRecord.RegistryStatus code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("SourcePassportRecord.RegistryStatus", name());
        }
    }
}
