package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QuoteMonitorInstrumentSelectionService {
    private final QuoteMonitorInstrumentSelectionRepository repository;
    private final QuoteMonitorInstrumentCandidatePort candidatePort;

    public QuoteMonitorInstrumentSelectionService(
            QuoteMonitorInstrumentSelectionRepository repository,
            QuoteMonitorInstrumentCandidatePort candidatePort
    ) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.candidatePort = Objects.requireNonNull(candidatePort, "candidatePort must not be null");
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

    public void replaceSelection(List<InstrumentCode> instrumentCodes) {
        QuoteMonitorInstrumentSelection selection = new QuoteMonitorInstrumentSelection(instrumentCodes);

        List<InstrumentCode> missingCandidateInstrumentCodes =
                candidatePort.findMissingCandidateInstrumentCodes(selection.instrumentCodes());

        if (!missingCandidateInstrumentCodes.isEmpty()) {
            throw new QuoteMonitorInstrumentCandidateNotFoundException(missingCandidateInstrumentCodes);
        }

        repository.replace(selection);
    }
}
