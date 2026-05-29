package com.alligator.market.domain.marketdata.feed.plan;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.plan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapturerFeedPlanTest {

    @Test
    void createsPlanForCapturerInstrumentAndPrioritizedSourceCodes() {
        CapturerCode capturerCode = CapturerCode.of("TEST_CAPTURER");
        InstrumentCode instrumentCode = InstrumentCode.of("EUR_USD");
        PrioritizedSourceCode backupSourceCode = prioritizedSourceCode("BACKUP_SOURCE", 1);
        PrioritizedSourceCode primarySourceCode = prioritizedSourceCode("PRIMARY_SOURCE", 0);

        CapturerFeedPlan plan = new CapturerFeedPlan(
                capturerCode,
                instrumentCode,
                List.of(backupSourceCode, primarySourceCode)
        );

        assertEquals(capturerCode, plan.capturerCode());
        assertEquals(instrumentCode, plan.instrumentCode());
        assertEquals(List.of(primarySourceCode, backupSourceCode), plan.prioritizedSourceCodes());
    }

    @Test
    void rejectsEmptyPrioritizedSourceCodes() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerFeedPlan(
                        CapturerCode.of("TEST_CAPTURER"),
                        InstrumentCode.of("EUR_USD"),
                        List.of()
                )
        );
    }

    @Test
    void rejectsDuplicateSourceCodes() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerFeedPlan(
                        CapturerCode.of("TEST_CAPTURER"),
                        InstrumentCode.of("EUR_USD"),
                        List.of(
                                prioritizedSourceCode("PRIMARY_SOURCE", 0),
                                prioritizedSourceCode("PRIMARY_SOURCE", 1)
                        )
                )
        );
    }

    @Test
    void rejectsDuplicatePriorities() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerFeedPlan(
                        CapturerCode.of("TEST_CAPTURER"),
                        InstrumentCode.of("EUR_USD"),
                        List.of(
                                prioritizedSourceCode("PRIMARY_SOURCE", 0),
                                prioritizedSourceCode("BACKUP_SOURCE", 0)
                        )
                )
        );
    }

    private static PrioritizedSourceCode prioritizedSourceCode(String sourceCode, int priority) {
        return new PrioritizedSourceCode(SourceCode.of(sourceCode), priority);
    }
}
