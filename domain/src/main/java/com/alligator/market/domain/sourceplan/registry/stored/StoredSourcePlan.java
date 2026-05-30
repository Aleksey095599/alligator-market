package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.shared.code.DomainCodeFormat;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record StoredSourcePlan(
        SourcePlan plan,
        ExecutionStatus executionStatus,
        List<Entry> entries
) {

    public StoredSourcePlan {
        Objects.requireNonNull(plan, "plan must not be null");
        Objects.requireNonNull(executionStatus, "executionStatus must not be null");
        Objects.requireNonNull(entries, "entries must not be null");

        if (entries.isEmpty()) {
            throw new IllegalArgumentException("entries must not be empty");
        }

        entries = List.copyOf(entries);
        requireEntriesMatchPlan(plan, entries);
    }

    public static StoredSourcePlan available(SourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        List<Entry> entries = new ArrayList<>(plan.prioritizedSourceCodes().size());
        for (PrioritizedSourceCode prioritizedSourceCode : plan.prioritizedSourceCodes()) {
            entries.add(Entry.available(prioritizedSourceCode));
        }

        return new StoredSourcePlan(plan, ExecutionStatus.AVAILABLE, entries);
    }

    public CapturerCode capturerCode() {
        return plan.capturerCode();
    }

    public InstrumentCode instrumentCode() {
        return plan.instrumentCode();
    }

    private static void requireEntriesMatchPlan(
            SourcePlan plan,
            List<Entry> entries
    ) {
        List<PrioritizedSourceCode> entrySourceCodes = entries.stream()
                .map(Entry::prioritizedSourceCode)
                .toList();

        if (!entrySourceCodes.equals(plan.prioritizedSourceCodes())) {
            throw new IllegalArgumentException("entries must match plan prioritized source codes");
        }
    }

    public record Entry(
            PrioritizedSourceCode prioritizedSourceCode,
            EntryLifecycleStatus lifecycleStatus
    ) {

        public Entry {
            Objects.requireNonNull(prioritizedSourceCode, "prioritizedSourceCode must not be null");
            Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
        }

        public static Entry available(PrioritizedSourceCode prioritizedSourceCode) {
            return new Entry(prioritizedSourceCode, EntryLifecycleStatus.AVAILABLE);
        }
    }

    public enum ExecutionStatus {
        AVAILABLE,
        CAPTURER_RETIRED,
        NO_AVAILABLE_SOURCES;

        private static final int MAX_CODE_LENGTH = 20;

        ExecutionStatus() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "StoredSourcePlan.ExecutionStatus code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("StoredSourcePlan.ExecutionStatus", name());
        }
    }

    public enum EntryLifecycleStatus {
        AVAILABLE,
        SOURCE_RETIRED;

        private static final int MAX_CODE_LENGTH = 14;

        EntryLifecycleStatus() {
            if (name().length() > MAX_CODE_LENGTH) {
                throw new IllegalStateException(
                        "StoredSourcePlan.EntryLifecycleStatus code must not exceed " +
                                MAX_CODE_LENGTH + " characters: " + name()
                );
            }
            DomainCodeFormat.requireValidEnumCode("StoredSourcePlan.EntryLifecycleStatus", name());
        }
    }
}
