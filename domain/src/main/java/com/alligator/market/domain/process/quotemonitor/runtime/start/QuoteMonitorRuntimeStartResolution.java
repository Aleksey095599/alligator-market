package com.alligator.market.domain.process.quotemonitor.runtime.start;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QuoteMonitorRuntimeStartResolution {
    private final QuoteMonitorRuntimeState initialState;
    private final List<QuoteMonitorRuntimeSourceAssignment> sourceAssignments;

    private QuoteMonitorRuntimeStartResolution(
            QuoteMonitorRuntimeState initialState,
            List<QuoteMonitorRuntimeSourceAssignment> sourceAssignments
    ) {
        this.initialState = Objects.requireNonNull(initialState, "initialState must not be null");
        this.sourceAssignments = List.copyOf(
                Objects.requireNonNull(sourceAssignments, "sourceAssignments must not be null")
        );
    }

    public static QuoteMonitorRuntimeStartResolution running(
            List<QuoteMonitorRuntimeSourceAssignment> sourceAssignments,
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        List<QuoteMonitorRuntimeSourceAssignment> assignmentsToStart =
                List.copyOf(Objects.requireNonNull(sourceAssignments, "sourceAssignments must not be null"));
        List<QuoteMonitorInstrumentRuntimeState> states =
                List.copyOf(Objects.requireNonNull(instrumentStates, "instrumentStates must not be null"));

        return new QuoteMonitorRuntimeStartResolution(
                new QuoteMonitorRuntimeState(
                        QuoteMonitorRuntimeStatus.RUNNING,
                        monitoredInstrumentCodes(assignmentsToStart),
                        null,
                        states
                ),
                assignmentsToStart
        );
    }

    public QuoteMonitorRuntimeState initialState() {
        return initialState;
    }

    public List<QuoteMonitorRuntimeSourceAssignment> sourceAssignments() {
        return sourceAssignments;
    }

    public List<InstrumentCode> monitoredInstrumentCodes() {
        return initialState.monitoredInstrumentCodes();
    }

    private static List<InstrumentCode> monitoredInstrumentCodes(
            List<QuoteMonitorRuntimeSourceAssignment> assignments
    ) {
        Set<InstrumentCode> codes = new LinkedHashSet<>();

        for (QuoteMonitorRuntimeSourceAssignment assignment : assignments) {
            codes.add(assignment.instrument().instrumentCode());
        }

        return List.copyOf(codes);
    }
}
