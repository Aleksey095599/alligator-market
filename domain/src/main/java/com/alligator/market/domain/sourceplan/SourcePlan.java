package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.*;

@SuppressWarnings("ClassCanBeRecord") // Domain model, not a DTO-style data carrier.
public final class SourcePlan {

    private final SourcePlanKey key;
    private final List<PrioritizedSourceCode> prioritizedSourceCodes;

    public SourcePlan(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode,
            List<PrioritizedSourceCode> prioritizedSourceCodes
    ) {
        this(new SourcePlanKey(capturerCode, instrumentCode), prioritizedSourceCodes);
    }

    public SourcePlan(
            SourcePlanKey key,
            List<PrioritizedSourceCode> prioritizedSourceCodes
    ) {
        this.key = Objects.requireNonNull(key, "key must not be null");
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

            if (!sourceCodes.add(prioritizedSourceCodeToCheck.sourceCode())) {
                throw new IllegalArgumentException(
                        "Source plan contains duplicate source code '" +
                                prioritizedSourceCodeToCheck.sourceCode().value() + "'"
                );
            }

            if (!priorities.add(prioritizedSourceCodeToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Source plan contains duplicate priority '" +
                                prioritizedSourceCodeToCheck.priority() + "'"
                );
            }

            validatedSourceCodes.add(prioritizedSourceCodeToCheck);
        }

        return List.copyOf(validatedSourceCodes);
    }

    public CapturerCode capturerCode() {
        return key.capturerCode();
    }

    public InstrumentCode instrumentCode() {
        return key.instrumentCode();
    }

    public SourcePlanKey key() {
        return key;
    }

    public List<PrioritizedSourceCode> prioritizedSourceCodes() {
        return prioritizedSourceCodes;
    }
}
