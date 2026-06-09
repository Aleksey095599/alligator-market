package com.alligator.market.domain.source.handler.passport.store;

import com.alligator.market.domain.shared.code.DomainCodeFormat;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record SourceHandlerPassportRecord(
        SourceCode sourceCode,
        HandlerCode handlerCode,
        SourceHandlerPassport passport,
        RegistryStatus registryStatus
) {

    public SourceHandlerPassportRecord {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }

    public static SourceHandlerPassportRecord registered(
            SourceCode sourceCode,
            HandlerCode handlerCode,
            SourceHandlerPassport passport
    ) {
        return new SourceHandlerPassportRecord(
                sourceCode,
                handlerCode,
                passport,
                RegistryStatus.REGISTERED
        );
    }

    public static SourceHandlerPassportRecord retired(
            SourceCode sourceCode,
            HandlerCode handlerCode,
            SourceHandlerPassport passport
    ) {
        return new SourceHandlerPassportRecord(
                sourceCode,
                handlerCode,
                passport,
                RegistryStatus.RETIRED
        );
    }

    public SourceHandlerPassportKey key() {
        return new SourceHandlerPassportKey(sourceCode, handlerCode);
    }

    public enum RegistryStatus {
        REGISTERED,
        RETIRED;

        private static final int MAX_CODE_LENGTH = 10;

        RegistryStatus() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "SourceHandlerPassportRecord.RegistryStatus code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("SourceHandlerPassportRecord.RegistryStatus", name());
        }
    }
}
