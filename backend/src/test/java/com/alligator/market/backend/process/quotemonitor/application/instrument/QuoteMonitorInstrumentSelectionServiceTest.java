package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentSelectionLockedException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorSelectedInstrument;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                new FakeSourcePlanQueryPort(Map.of()),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(),
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
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
                new FakeSourcePlanQueryPort(Map.of()),
                runtimeRegistryUpdater,
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
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
                new FakeSourcePlanQueryPort(Map.of()),
                runtimeRegistryUpdater,
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
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
                new FakeSourcePlanQueryPort(Map.of()),
                runtimeRegistryUpdater,
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
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
                new FakeSourcePlanQueryPort(Map.of()),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(),
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
        );

        assertThatThrownBy(() -> service.replaceSelection(List.of(
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"),
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
        )))
                .isInstanceOf(DuplicateQuoteMonitorInstrumentCodeException.class);

        assertThat(repository.get().instrumentCodes()).isEmpty();
    }

    @Test
    void rejectsInstrumentCodesWithoutQuoteMonitorSourcePlan() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                )),
                new FakeSourcePlanQueryPort(Map.of()),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(),
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
        );

        assertThatThrownBy(() -> service.replaceSelection(List.of(
                new InstrumentCode("FOREX_SPOT_USDRUB_TOM")
        )))
                .isInstanceOf(QuoteMonitorInstrumentCandidateNotFoundException.class);

        assertThat(repository.get().instrumentCodes()).isEmpty();
    }

    @Test
    void rejectsSelectionChangesWhenRuntimeIsRunning() {
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(QuoteMonitorInstrumentSelection.empty());
        FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater();
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of(
                        new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
                )),
                new FakeSourcePlanQueryPort(Map.of()),
                runtimeRegistryUpdater,
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.RUNNING)
        );

        assertThatThrownBy(() -> service.addInstrument(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")))
                .isInstanceOf(QuoteMonitorInstrumentSelectionLockedException.class);
        assertThatThrownBy(() -> service.removeInstrument(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")))
                .isInstanceOf(QuoteMonitorInstrumentSelectionLockedException.class);
        assertThatThrownBy(() -> service.replaceSelection(List.of(
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
        )))
                .isInstanceOf(QuoteMonitorInstrumentSelectionLockedException.class);

        assertThat(repository.get().instrumentCodes()).isEmpty();
        assertThat(runtimeRegistryUpdater.updateCount).isZero();
    }

    @Test
    void returnsSelectedInstrumentsWithSourcePlanStatuses() {
        InstrumentCode firstCode = new InstrumentCode("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode secondCode = new InstrumentCode("FOREX_SPOT_USDRUB_TOM");
        FakeQuoteMonitorInstrumentSelectionRepository repository =
                new FakeQuoteMonitorInstrumentSelectionRepository(
                        new QuoteMonitorInstrumentSelection(List.of(firstCode, secondCode))
                );
        QuoteMonitorInstrumentSelectionService service = new QuoteMonitorInstrumentSelectionService(
                repository,
                new FakeQuoteMonitorInstrumentCandidatePort(List.of()),
                new FakeSourcePlanQueryPort(Map.of(
                        firstCode, StoredSourcePlanExecutionStatus.AVAILABLE,
                        secondCode, StoredSourcePlanExecutionStatus.NO_AVAILABLE_SOURCES
                )),
                new FakeRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(),
                new FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus.STOPPED)
        );

        assertThat(service.findSelectedInstruments())
                .containsExactly(
                        new QuoteMonitorSelectedInstrument(
                                firstCode,
                                StoredSourcePlanExecutionStatus.AVAILABLE
                        ),
                        new QuoteMonitorSelectedInstrument(
                                secondCode,
                                StoredSourcePlanExecutionStatus.NO_AVAILABLE_SOURCES
                        )
                );
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

    private static final class FakeSourcePlanQueryPort implements SourcePlanQueryPort {
        private final Map<InstrumentCode, StoredSourcePlanExecutionStatus> sourcePlanStatuses;

        private FakeSourcePlanQueryPort(
                Map<InstrumentCode, StoredSourcePlanExecutionStatus> sourcePlanStatuses
        ) {
            this.sourcePlanStatuses = Map.copyOf(sourcePlanStatuses);
        }

        @Override
        public Optional<SourcePlanQueryItem> findByMarketDataCapturerCodeAndInstrumentCode(
                CapturerCode capturerCode,
                InstrumentCode instrumentCode
        ) {
            throw new UnsupportedOperationException("Not needed by this test");
        }

        @Override
        public Map<InstrumentCode, StoredSourcePlanExecutionStatus>
                findExecutionStatusesByMarketDataCapturerCodeAndInstrumentCodes(
                        CapturerCode capturerCode,
                        List<InstrumentCode> instrumentCodes
                ) {
            Map<InstrumentCode, StoredSourcePlanExecutionStatus> statuses = new LinkedHashMap<>();

            for (InstrumentCode instrumentCode : instrumentCodes) {
                StoredSourcePlanExecutionStatus status = sourcePlanStatuses.get(instrumentCode);
                if (status != null) {
                    statuses.put(instrumentCode, status);
                }
            }

            return statuses;
        }

        @Override
        public List<SourcePlanQueryItem> findAll() {
            throw new UnsupportedOperationException("Not needed by this test");
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

    private static final class FakeQuoteMonitorRuntimeProcess implements QuoteMonitorRuntimeProcess {
        private QuoteMonitorRuntimeStatus status;

        private FakeQuoteMonitorRuntimeProcess(QuoteMonitorRuntimeStatus status) {
            this.status = status;
        }

        @Override
        public boolean start() {
            status = QuoteMonitorRuntimeStatus.RUNNING;
            return true;
        }

        @Override
        public boolean stop() {
            status = QuoteMonitorRuntimeStatus.STOPPED;
            return true;
        }

        @Override
        public QuoteMonitorRuntimeStatus status() {
            return status;
        }

        @Override
        public QuoteMonitorRuntimeState state() {
            return new QuoteMonitorRuntimeState(status, List.of(), null);
        }
    }
}
