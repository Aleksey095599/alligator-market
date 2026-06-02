package com.alligator.market.domain.source.passport.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record StoredSourcePassport(
        SourceCode sourceCode,
        SourcePassport passport,
        RegistryStatus registryStatus
) {

    public StoredSourcePassport {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }

    public static StoredSourcePassport registered(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new StoredSourcePassport(sourceCode, passport, RegistryStatus.REGISTERED);
    }

    public static StoredSourcePassport retired(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new StoredSourcePassport(sourceCode, passport, RegistryStatus.RETIRED);
    }

    public enum RegistryStatus {
        REGISTERED,
        RETIRED;

        private static final int MAX_CODE_LENGTH = 10;

        RegistryStatus() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "StoredSourcePassport.RegistryStatus code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("StoredSourcePassport.RegistryStatus", name());
        }
    }
}
