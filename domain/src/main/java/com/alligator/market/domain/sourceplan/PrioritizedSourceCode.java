package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public record PrioritizedSourceCode(
        SourceCode sourceCode,
        int priority
) {
    public PrioritizedSourceCode {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
