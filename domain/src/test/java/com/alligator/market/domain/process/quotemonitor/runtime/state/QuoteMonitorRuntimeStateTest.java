package com.alligator.market.domain.process.quotemonitor.runtime.state;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuoteMonitorRuntimeStateTest {

    @Test
    void findsInstrumentStateByInstrumentCode() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        QuoteMonitorInstrumentRuntimeState instrumentState =
                QuoteMonitorInstrumentRuntimeState.waitingForQuote(
                        instrumentCode,
                        SourceCode.of("MOEX_ISS")
                );
        QuoteMonitorRuntimeState state = new QuoteMonitorRuntimeState(
                QuoteMonitorRuntimeStatus.RUNNING,
                List.of(instrumentCode),
                null,
                List.of(instrumentState)
        );

        assertEquals(
                instrumentState,
                state.instrumentState(instrumentCode).orElseThrow()
        );
        assertTrue(state.instrumentState(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM")).isEmpty());
    }

    @Test
    void returnsIssueInstrumentStates() {
        QuoteMonitorInstrumentRuntimeState liveState =
                QuoteMonitorInstrumentRuntimeState.live(
                        InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM"),
                        SourceCode.of("MOEX_ISS")
                );
        QuoteMonitorInstrumentRuntimeState issueState =
                QuoteMonitorInstrumentRuntimeState.issue(
                        InstrumentCode.of("FOREX_SPOT_USDRUB_TOM"),
                        SourceCode.of("MOEX_ISS"),
                        QuoteMonitorInstrumentRuntimeStatus.HANDLER_NOT_FOUND,
                        "Handler not found"
                );
        QuoteMonitorRuntimeState state = new QuoteMonitorRuntimeState(
                QuoteMonitorRuntimeStatus.RUNNING,
                List.of(liveState.instrumentCode(), issueState.instrumentCode()),
                null,
                List.of(liveState, issueState)
        );

        assertTrue(state.hasInstrumentIssues());
        assertEquals(List.of(issueState), state.issueInstrumentStates());
    }

    @Test
    void returnsNoIssuesWhenInstrumentStatesAreOperational() {
        QuoteMonitorRuntimeState state = new QuoteMonitorRuntimeState(
                QuoteMonitorRuntimeStatus.RUNNING,
                List.of(InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM")),
                null,
                List.of(QuoteMonitorInstrumentRuntimeState.waitingForQuote(
                        InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM"),
                        SourceCode.of("MOEX_ISS")
                ))
        );

        assertFalse(state.hasInstrumentIssues());
        assertTrue(state.issueInstrumentStates().isEmpty());
    }

    @Test
    void rejectsDuplicateInstrumentStates() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");

        assertThrows(
                IllegalArgumentException.class,
                () -> new QuoteMonitorRuntimeState(
                        QuoteMonitorRuntimeStatus.RUNNING,
                        List.of(instrumentCode),
                        null,
                        List.of(
                                QuoteMonitorInstrumentRuntimeState.waitingForQuote(
                                        instrumentCode,
                                        SourceCode.of("MOEX_ISS")
                                ),
                                QuoteMonitorInstrumentRuntimeState.live(
                                        instrumentCode,
                                        SourceCode.of("MOEX_ISS")
                                )
                        )
                )
        );
    }
}
