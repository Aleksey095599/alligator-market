package com.alligator.market.domain.process.quotemonitor.runtime.start;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface QuoteMonitorRuntimeStart {

    default QuoteMonitorRuntimeStartResolution resolve() {
        List<QuoteMonitorRuntimeSourceAssignment> sourceAssignments = new ArrayList<>();
        List<QuoteMonitorInstrumentRuntimeState> instrumentStates = new ArrayList<>();

        for (InstrumentCode instrumentCode : selectedInstrumentCodes()) {
            Optional<Instrument> instrument = findInstrumentInRuntimeRegistry(instrumentCode);
            if (instrument.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        QuoteMonitorInstrumentRuntimeStatus.RUNTIME_INSTRUMENT_NOT_FOUND,
                        "Selected instrument is absent from runtime instrument registry"
                ));
                continue;
            }

            Optional<SourcePlan> sourcePlan = findAvailableSourcePlan(instrumentCode);
            if (sourcePlan.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_PLAN_NOT_FOUND,
                        "Available source plan is absent from runtime source plan registry"
                ));
                continue;
            }

            Optional<QuoteMonitorRuntimeSourceAssignment> sourceAssignment =
                    resolveSourceAssignmentFromPlan(instrument.get(), sourcePlan.get());
            if (sourceAssignment.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_NOT_FOUND,
                        "Source plan has no entries available in runtime source registry"
                ));
                continue;
            }

            QuoteMonitorRuntimeSourceAssignment assignmentToStart = sourceAssignment.get();
            sourceAssignments.add(assignmentToStart);
            instrumentStates.add(QuoteMonitorInstrumentRuntimeState.waitingForQuote(
                    instrumentCode,
                    assignmentToStart.sourceCode()
            ));
        }

        return QuoteMonitorRuntimeStartResolution.running(sourceAssignments, instrumentStates);
    }

    List<InstrumentCode> selectedInstrumentCodes();

    Optional<Instrument> findInstrumentInRuntimeRegistry(InstrumentCode instrumentCode);

    Optional<SourcePlan> findAvailableSourcePlan(InstrumentCode instrumentCode);

    Optional<QuoteMonitorRuntimeSourceAssignment> resolveSourceAssignmentFromPlan(
            Instrument instrument,
            SourcePlan sourcePlan
    );

    private static QuoteMonitorInstrumentRuntimeState runtimeIssue(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            QuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        return QuoteMonitorInstrumentRuntimeState.issue(
                instrumentCode,
                sourceCode,
                status,
                detail
        );
    }
}
