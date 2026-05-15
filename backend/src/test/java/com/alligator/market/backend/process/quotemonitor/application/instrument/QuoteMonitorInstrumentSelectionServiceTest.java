package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuoteMonitorInstrumentSelectionServiceTest {

    @Test
    void returnsCandidateOptionsWithSelectionState() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(
                        new QuoteMonitorInstrumentSelection(List.of(
                                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                        ))
                );
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"),
                        new InstrumentCode("FOREX_SPOT_USDRUB_TOM")
                ))
        );

        assertThat(service.findOptions())
                .containsExactly(
                        new QuoteMonitorInstrumentOption(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"), true),
                        new QuoteMonitorInstrumentOption(new InstrumentCode("FOREX_SPOT_USDRUB_TOM"), false)
                );
    }

    @Test
    void replacesSelectionWhenAllInstrumentCodesAreCandidates() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                ))
        );

        service.replaceSelection(List.of(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")));

        assertThat(repository.get().instrumentCodes())
                .containsExactly(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"));
    }

    @Test
    void rejectsDuplicateInstrumentCodes() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                ))
        );

        assertThatThrownBy(() -> service.replaceSelection(List.of(
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"),
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
        )))
                .isInstanceOf(DuplicateQuoteMonitorInstrumentCodeException.class);

        assertThat(repository.get().instrumentCodes()).isEmpty();
    }

    @Test
    void rejectsInstrumentCodesWithoutLiveQuoteMonitorSourcePlan() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                ))
        );

        assertThatThrownBy(() -> service.replaceSelection(List.of(
                new InstrumentCode("FOREX_SPOT_USDRUB_TOM")
        )))
                .isInstanceOf(QuoteMonitorInstrumentCandidateNotFoundException.class);

        assertThat(repository.get().instrumentCodes()).isEmpty();
    }

    private static final class FakeQuoteMonitorInstrumentSelectionRepository
            implements QuoteMonitorInstrumentSelectionRepository {
        private QuoteMonitorInstrumentSelection selection;

        private FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection selection) {
            this.selection = selection;
        }

        @Override
        public QuoteMonitorInstrumentSelection get() {
            return selection;
        }

        @Override
        public void replace(QuoteMonitorInstrumentSelection selection) {
            this.selection = selection;
        }
    }

    private static final class FakeQuoteMonitorInstrumentCandidatePort
            implements QuoteMonitorInstrumentCandidatePort {
        private final List<InstrumentCode> candidateInstrumentCodes;
        private final Set<InstrumentCode> candidateInstrumentCodeSet;

        private FakeQuoteMonitorInstrumentCandidatePort(List<InstrumentCode> candidateInstrumentCodes) {
            this.candidateInstrumentCodes = List.copyOf(candidateInstrumentCodes);
            this.candidateInstrumentCodeSet = Set.copyOf(candidateInstrumentCodes);
        }

        @Override
        public List<InstrumentCode> findCandidateInstrumentCodes() {
            return candidateInstrumentCodes;
        }

        @Override
        public List<InstrumentCode> findMissingCandidateInstrumentCodes(List<InstrumentCode> instrumentCodes) {
            return instrumentCodes.stream()
                    .filter(instrumentCode -> !candidateInstrumentCodeSet.contains(instrumentCode))
                    .toList();
        }
    }
}
