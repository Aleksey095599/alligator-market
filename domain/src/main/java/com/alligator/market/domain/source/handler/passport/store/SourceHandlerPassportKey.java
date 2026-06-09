package com.alligator.market.domain.source.handler.passport.store;

import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record SourceHandlerPassportKey(
        SourceCode sourceCode,
        HandlerCode handlerCode
) {

    public SourceHandlerPassportKey {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }
}
