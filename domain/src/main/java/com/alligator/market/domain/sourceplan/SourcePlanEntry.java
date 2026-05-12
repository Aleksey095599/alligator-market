package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

/**
 * Lower priority values are preferred; 0 is the highest priority.
 */
public record SourcePlanEntry(
        SourceCode sourceCode,
        int priority
) {
    public SourcePlanEntry {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
