package com.alligator.market.domain.source.passport.registry.stored;

import com.alligator.market.domain.shared.code.DomainCodeFormat;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record StoredSourcePassport(
        SourceCode sourceCode,
        SourcePassport passport,
        Status status
) {

    public StoredSourcePassport {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }

    public static StoredSourcePassport registered(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new StoredSourcePassport(sourceCode, passport, Status.REGISTERED);
    }

    public static StoredSourcePassport retired(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new StoredSourcePassport(sourceCode, passport, Status.RETIRED);
    }

    public enum Status {
        REGISTERED,
        RETIRED;

        private static final int MAX_CODE_LENGTH = 10;

        Status() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "StoredSourcePassport.Status code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("StoredSourcePassport.Status", name());
        }
    }
}
