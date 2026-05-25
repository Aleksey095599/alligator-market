package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentSelectionLockedException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QuoteMonitorInstrumentSelectionService {
    private final QuoteMonitorInstrumentSelectionRepository repository;
    private final QuoteMonitorInstrumentCandidatePort candidatePort;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater;
    private final LiveQuoteMonitorRuntimeProcess runtimeProcess;

    public QuoteMonitorInstrumentSelectionService(
            QuoteMonitorInstrumentSelectionRepository repository,
            QuoteMonitorInstrumentCandidatePort candidatePort,
            RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater,
            LiveQuoteMonitorRuntimeProcess runtimeProcess
    ) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.candidatePort = Objects.requireNonNull(candidatePort, "candidatePort must not be null");
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
        if (runtimeProcess.status() == LiveQuoteMonitorRuntimeStatus.RUNNING) {
            throw new QuoteMonitorInstrumentSelectionLockedException();
        }
    }
}
