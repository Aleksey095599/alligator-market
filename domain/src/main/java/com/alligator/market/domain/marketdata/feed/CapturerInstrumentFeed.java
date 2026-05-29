package com.alligator.market.domain.marketdata.feed;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.marketdata.feed.vo.PrioritizedSource;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord") // Domain model, not a DTO-style data carrier.
public final class CapturerInstrumentFeed {
    private final CapturerCode capturerCode;
    private final InstrumentCode instrumentCode;
    private final List<PrioritizedSource> prioritizedSources;

    public CapturerInstrumentFeed(
            CapturerCode capturerCode,
            InstrumentCode instrumentCode,
            List<PrioritizedSource> prioritizedSources
    ) {
        this.capturerCode = Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.prioritizedSources = copyAndValidatePrioritizedSources(
                Objects.requireNonNull(prioritizedSources, "prioritizedSources must not be null")
        );
    }

    private static List<PrioritizedSource> copyAndValidatePrioritizedSources(
            List<PrioritizedSource> prioritizedSources
    ) {
        if (prioritizedSources.isEmpty()) {
            throw new IllegalArgumentException("prioritizedSources must not be empty");
        }

        List<PrioritizedSource> validatedSources = new ArrayList<>(prioritizedSources.size());
        Set<SourceCode> sourceCodes = new HashSet<>();
        Set<Integer> priorities = new HashSet<>();

        for (PrioritizedSource prioritizedSource : prioritizedSources) {
            PrioritizedSource prioritizedSourceToCheck = Objects.requireNonNull(
                    prioritizedSource,
                    "prioritizedSource must not be null"
            );

            SourceCode sourceCode = prioritizedSourceToCheck.source().code();
            if (!sourceCodes.add(sourceCode)) {
                throw new IllegalArgumentException(
                        "Capturer instrument feed contains duplicate source code '" + sourceCode.value() + "'"
                );
            }

            if (!priorities.add(prioritizedSourceToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Capturer instrument feed contains duplicate priority '" +
                                prioritizedSourceToCheck.priority() + "'"
                );
            }

            validatedSources.add(prioritizedSourceToCheck);
        }

        return List.copyOf(validatedSources);
    }

    public CapturerCode capturerCode() {
        return capturerCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<PrioritizedSource> prioritizedSources() {
        return prioritizedSources;
    }
}
