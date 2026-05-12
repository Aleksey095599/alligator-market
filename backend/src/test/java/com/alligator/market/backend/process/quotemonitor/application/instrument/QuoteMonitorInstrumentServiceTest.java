package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorSourcePlanNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuoteMonitorInstrumentServiceTest {

    @Test
    void replacesSelectedInstrumentCodesWhenAllSourcePlansExist() {
        FakeQuoteMonitorInstrumentPort port = new FakeQuoteMonitorInstrumentPort(
                Set.of(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"))
        );
        QuoteMonitorInstrumentService service = new QuoteMonitorInstrumentService(port);

        service.replaceSelectedInstrumentCodes(List.of(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")));

        assertThat(port.selectedInstrumentCodes)
                .containsExactly(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"));
    }

    @Test
    void rejectsDuplicateInstrumentCodes() {
        FakeQuoteMonitorInstrumentPort port = new FakeQuoteMonitorInstrumentPort(
                Set.of(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"))
        );
        QuoteMonitorInstrumentService service = new QuoteMonitorInstrumentService(port);

        assertThatThrownBy(() -> service.replaceSelectedInstrumentCodes(List.of(
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"),
                new InstrumentCode("FOREX_SPOT_CNYRUB_TOM")
        )))
                .isInstanceOf(DuplicateQuoteMonitorInstrumentCodeException.class);

        assertThat(port.selectedInstrumentCodes).isEmpty();
    }

    @Test
    void rejectsInstrumentCodesWithoutSourcePlan() {
        FakeQuoteMonitorInstrumentPort port = new FakeQuoteMonitorInstrumentPort(
                Set.of(new InstrumentCode("FOREX_SPOT_CNYRUB_TOM"))
        );
        QuoteMonitorInstrumentService service = new QuoteMonitorInstrumentService(port);

        assertThatThrownBy(() -> service.replaceSelectedInstrumentCodes(List.of(
                new InstrumentCode("FOREX_SPOT_USDRUB_TOM")
        )))
                .isInstanceOf(QuoteMonitorSourcePlanNotFoundException.class);

        assertThat(port.selectedInstrumentCodes).isEmpty();
    }

    private static final class FakeQuoteMonitorInstrumentPort implements QuoteMonitorInstrumentPort {
        private final Set<InstrumentCode> sourcePlanInstrumentCodes;
        private List<InstrumentCode> selectedInstrumentCodes = List.of();

        private FakeQuoteMonitorInstrumentPort(Set<InstrumentCode> sourcePlanInstrumentCodes) {
            this.sourcePlanInstrumentCodes = Set.copyOf(sourcePlanInstrumentCodes);
        }

        @Override
        public List<InstrumentCode> findAvailableInstrumentCodes() {
            return new ArrayList<>(sourcePlanInstrumentCodes);
        }

        @Override
        public List<InstrumentCode> findSelectedInstrumentCodes() {
            return selectedInstrumentCodes;
        }

        @Override
        public List<InstrumentCode> findInstrumentCodesWithoutSourcePlan(List<InstrumentCode> instrumentCodes) {
            return instrumentCodes.stream()
                    .filter(instrumentCode -> !sourcePlanInstrumentCodes.contains(instrumentCode))
                    .toList();
        }

        @Override
        public void replaceSelectedInstrumentCodes(List<InstrumentCode> instrumentCodes) {
            selectedInstrumentCodes = List.copyOf(instrumentCodes);
        }
    }
}
