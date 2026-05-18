package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QuoteMonitorInstrumentSelectionService {
    private final QuoteMonitorInstrumentSelectionRepository repository;
    private final QuoteMonitorInstrumentCandidatePort candidatePort;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater;

    public QuoteMonitorInstrumentSelectionService(
            QuoteMonitorInstrumentSelectionRepository repository,
            QuoteMonitorInstrumentCandidatePort candidatePort,
            RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater
    ) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.candidatePort = Objects.requireNonNull(candidatePort, "candidatePort must not be null");
        this.runtimeRegistryUpdater = Objects.requireNonNull(
                runtimeRegistryUpdater,
                "runtimeRegistryUpdater must not be null"
        );
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

        ensureInstrumentCodesAreCandidates(List.of(instrumentCode));

        boolean changed = repository.addIfAbsent(instrumentCode);
        if (changed) {
            updateRuntimeRegistry();
        }

        return changed;
    }

    public boolean removeInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        boolean changed = repository.removeIfExists(instrumentCode);
        if (changed) {
            updateRuntimeRegistry();
        }

        return changed;
    }

    public void replaceSelection(List<InstrumentCode> instrumentCodes) {
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
}
