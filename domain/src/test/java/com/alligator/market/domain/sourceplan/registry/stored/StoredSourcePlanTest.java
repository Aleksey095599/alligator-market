package com.alligator.market.domain.sourceplan.registry.stored;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StoredSourcePlanTest {

    @Test
    void createsAvailableStoredPlanFromSourcePlan() {
        SourcePlan plan = plan(
                source("MOEX", 0),
                source("TWELVE_DATA", 1)
        );

        StoredSourcePlan storedPlan = StoredSourcePlan.available(plan);

        assertEquals(plan, storedPlan.plan());
        assertEquals(StoredSourcePlan.ExecutionStatus.AVAILABLE, storedPlan.executionStatus());
        assertEquals(List.of(
                StoredSourcePlan.Entry.available(source("MOEX", 0)),
                StoredSourcePlan.Entry.available(source("TWELVE_DATA", 1))
        ), storedPlan.entries());
    }

    @Test
    void rejectsEntriesThatDoNotMatchPlanPrioritizedSourceCodes() {
        SourcePlan plan = plan(source("MOEX", 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> new StoredSourcePlan(
                        plan,
                        StoredSourcePlan.ExecutionStatus.AVAILABLE,
                        List.of(StoredSourcePlan.Entry.available(source("TWELVE_DATA", 0)))
                )
        );
    }

    private static SourcePlan plan(PrioritizedSourceCode... sourceCodes) {
        return new SourcePlan(
                new SourcePlanKey(
                        CapturerCode.of("QUOTE_MONITOR"),
                        InstrumentCode.of("FOREX_SPOT_USDRUB_TOM")
                ),
                List.of(sourceCodes)
        );
    }

    private static PrioritizedSourceCode source(String sourceCode, int priority) {
        return new PrioritizedSourceCode(SourceCode.of(sourceCode), priority);
    }
}
