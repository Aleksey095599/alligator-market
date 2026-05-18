package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
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
                )),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater()
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
        FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater();
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                )),
                runtimeRegistryUpdater
        );

        service.replaceSelection(List.of(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")));

        assertThat(repository.get().instrumentCodes())
                .containsExactly(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"));
        assertThat(runtimeRegistryUpdater.updateCount).isEqualTo(1);
    }

    @Test
    void addsInstrumentWhenInstrumentCodeIsCandidate() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater();
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                )),
                runtimeRegistryUpdater
        );

        boolean changed = service.addInstrument(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"));

        assertThat(changed).isTrue();
        assertThat(repository.get().instrumentCodes())
                .containsExactly(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"));
        assertThat(runtimeRegistryUpdater.updateCount).isEqualTo(1);
    }

    @Test
    void removesInstrumentWhenInstrumentIsSelected() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(
                        new QuoteMonitorInstrumentSelection(List.of(
                                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                        ))
                );
        FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater();
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of()),
                runtimeRegistryUpdater
        );

        boolean changed = service.removeInstrument(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"));

        assertThat(changed).isTrue();
        assertThat(repository.get().instrumentCodes()).isEmpty();
        assertThat(runtimeRegistryUpdater.updateCount).isEqualTo(1);
    }

    @Test
    void rejectsDuplicateInstrumentCodes() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                )),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater()
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
                )),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater()
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
        public boolean addIfAbsent(InstrumentCode instrumentCode) {
            if (selection.contains(instrumentCode)) {
                return false;
            }

            selection = selection.withInstrument(instrumentCode);
            return true;
        }

        @Override
        public boolean removeIfExists(InstrumentCode instrumentCode) {
            if (!selection.contains(instrumentCode)) {
                return false;
            }

            selection = selection.withoutInstrument(instrumentCode);
            return true;
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

    private static final class FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater
            implements RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater {
        private int updateCount;

        @Override
        public void updateRuntimeRegistry() {
            updateCount++;
        }
    }
}
