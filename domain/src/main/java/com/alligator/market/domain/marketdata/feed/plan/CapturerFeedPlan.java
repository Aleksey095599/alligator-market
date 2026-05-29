package com.alligator.market.domain.marketdata.feed.plan;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.plan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record CapturerFeedPlan(
        CapturerCode capturerCode,
        InstrumentCode instrumentCode,
        List<PrioritizedSourceCode> prioritizedSourceCodes
) {
    public CapturerFeedPlan(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode,
            List<PrioritizedSourceCode> prioritizedSourceCodes
    ) {
        this.capturerCode = Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.prioritizedSourceCodes = copyAndValidatePrioritizedSourceCodes(
                Objects.requireNonNull(prioritizedSourceCodes, "prioritizedSourceCodes must not be null")
        );
    }

    private static List<PrioritizedSourceCode> copyAndValidatePrioritizedSourceCodes(
            List<PrioritizedSourceCode> prioritizedSourceCodes
    ) {
        if (prioritizedSourceCodes.isEmpty()) {
            throw new IllegalArgumentException("prioritizedSourceCodes must not be empty");
        }

        List<PrioritizedSourceCode> validatedSourceCodes = new ArrayList<>(prioritizedSourceCodes.size());
        Set<SourceCode> sourceCodes = new HashSet<>();
        Set<Integer> priorities = new HashSet<>();

        for (PrioritizedSourceCode prioritizedSourceCode : prioritizedSourceCodes) {
            PrioritizedSourceCode prioritizedSourceCodeToCheck = Objects.requireNonNull(
                    prioritizedSourceCode,
                    "prioritizedSourceCode must not be null"
            );

            SourceCode sourceCode = prioritizedSourceCodeToCheck.sourceCode();
            if (!sourceCodes.add(sourceCode)) {
                throw new IllegalArgumentException(
                        "Capturer feed plan contains duplicate source code '" + sourceCode.value() + "'"
                );
            }

            if (!priorities.add(prioritizedSourceCodeToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Capturer feed plan contains duplicate priority '" +
                                prioritizedSourceCodeToCheck.priority() + "'"
                );
            }

            validatedSourceCodes.add(prioritizedSourceCodeToCheck);
        }

        validatedSourceCodes.sort(Comparator.comparingInt(PrioritizedSourceCode::priority));
        return List.copyOf(validatedSourceCodes);
    }
}
