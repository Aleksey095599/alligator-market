package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentSelectionLockedException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorSelectedInstrument;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class QuoteMonitorInstrumentSelectionService {
    private final QuoteMonitorInstrumentSelectionRepository repository;
    private final QuoteMonitorInstrumentCandidatePort candidatePort;
    private final SourcePlanQueryPort sourcePlanQueryPort;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater;
    private final QuoteMonitorRuntimeProcess runtimeProcess;

    public QuoteMonitorInstrumentSelectionService(
            QuoteMonitorInstrumentSelectionRepository repository,
            QuoteMonitorInstrumentCandidatePort candidatePort,
            SourcePlanQueryPort sourcePlanQueryPort,
            RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater,
            QuoteMonitorRuntimeProcess runtimeProcess
    ) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.candidatePort = Objects.requireNonNull(candidatePort, "candidatePort must not be null");
        this.sourcePlanQueryPort = Objects.requireNonNull(
                sourcePlanQueryPort,
                "sourcePlanQueryPort must not be null"
        );
        this.runtimeRegistryUpdater = Objects.requireNonNull(
                runtimeRegistryUpdater,
                "runtimeRegistryUpdater must not be null"
        );
        this.runtimeProcess = Objects.requireNonNull(runtimeProcess, "runtimeProcess must not be null");
    }

    public List<QuoteMonitorInstrumentOption> findOptions() {
        List<InstrumentCode> candidateInstrumentCodes = candidatePort.findCandidateInstrumentCodes();
        Set<InstrumentCode> selectedInstrumentCodes = new HashSet<>(getSelection().instrumentCodes());

        return candidateInstrumentCodes.stream()
                .map(instrumentCode -> new QuoteMonitorInstrumentOption(
                        instrumentCode,
                        selectedInstrumentCodes.contains(instrumentCode)
                ))
                .toList();
    }

    public QuoteMonitorInstrumentSelection getSelection() {
        return repository.get();
    }

    public List<QuoteMonitorSelectedInstrument> findSelectedInstruments() {
        List<InstrumentCode> selectedInstrumentCodes = getSelection().instrumentCodes();
        Map<InstrumentCode, StoredSourcePlanExecutionStatus> sourcePlanStatuses =
                sourcePlanQueryPort.findExecutionStatusesByMarketDataCapturerCodeAndInstrumentCodes(
                        QuoteMonitorCapturer.CAPTURER_CODE,
                        selectedInstrumentCodes
                );

        return selectedInstrumentCodes.stream()
                .map(instrumentCode -> new QuoteMonitorSelectedInstrument(
                        instrumentCode,
                        requireSourcePlanStatus(instrumentCode, sourcePlanStatuses)
                ))
                .toList();
    }

    public boolean addInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        ensureRuntimeStopped();
        ensureInstrumentCodesAreCandidates(List.of(instrumentCode));

        boolean changed = repository.addIfAbsent(instrumentCode);
        if (changed) {
            updateRuntimeRegistry();
        }

        return changed;
    }

    public boolean removeInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        ensureRuntimeStopped();
        boolean changed = repository.removeIfExists(instrumentCode);
        if (changed) {
            updateRuntimeRegistry();
        }

        return changed;
    }

    public void replaceSelection(List<InstrumentCode> instrumentCodes) {
        ensureRuntimeStopped();

        QuoteMonitorInstrumentSelection selection = new QuoteMonitorInstrumentSelection(instrumentCodes);

        ensureInstrumentCodesAreCandidates(selection.instrumentCodes());

        repository.replace(selection);
        updateRuntimeRegistry();
    }

    private void ensureInstrumentCodesAreCandidates(List<InstrumentCode> instrumentCodes) {
        List<InstrumentCode> missingCandidateInstrumentCodes =
                candidatePort.findMissingCandidateInstrumentCodes(instrumentCodes);

        if (!missingCandidateInstrumentCodes.isEmpty()) {
            throw new QuoteMonitorInstrumentCandidateNotFoundException(missingCandidateInstrumentCodes);
        }
    }

    private void updateRuntimeRegistry() {
        runtimeRegistryUpdater.updateRuntimeRegistry();
    }

    private void ensureRuntimeStopped() {
        if (runtimeProcess.status() == QuoteMonitorRuntimeStatus.RUNNING) {
            throw new QuoteMonitorInstrumentSelectionLockedException();
        }
    }

    private StoredSourcePlanExecutionStatus requireSourcePlanStatus(
            InstrumentCode instrumentCode,
            Map<InstrumentCode, StoredSourcePlanExecutionStatus> sourcePlanStatuses
    ) {
        StoredSourcePlanExecutionStatus status = sourcePlanStatuses.get(instrumentCode);
        if (status == null) {
            throw new IllegalStateException(
                    "Source plan status is missing for selected quote monitor instrument: " +
                            instrumentCode.value()
            );
        }

        return status;
    }
}
