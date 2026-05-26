package com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanExecutionStatus;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqQuoteMonitorInstrumentCandidateAdapter implements QuoteMonitorInstrumentCandidatePort {
    private static final Table<?> SOURCE_PLAN = table(name("source_plan"));
    private static final Field<String> SOURCE_PLAN_CAPTURER_CODE =
            field(name("source_plan", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_INSTRUMENT_CODE =
            field(name("source_plan", "instrument_code"), String.class);
    private static final Field<String> SOURCE_PLAN_EXECUTION_STATUS =
            field(name("source_plan", "execution_status"), String.class);

    private final DSLContext dsl;

    public JooqQuoteMonitorInstrumentCandidateAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<InstrumentCode> findCandidateInstrumentCodes() {
        return dsl.select(SOURCE_PLAN_INSTRUMENT_CODE)
                .from(SOURCE_PLAN)
                .where(SOURCE_PLAN_CAPTURER_CODE.eq(QuoteMonitorCapturer.CAPTURER_CODE.value()))
                .and(SOURCE_PLAN_EXECUTION_STATUS.eq(
                        StoredSourcePlanExecutionStatus.AVAILABLE.name()))
                .orderBy(SOURCE_PLAN_INSTRUMENT_CODE.asc())
                .fetch(record -> new InstrumentCode(record.get(SOURCE_PLAN_INSTRUMENT_CODE)));
    }

    @Override
    public List<InstrumentCode> findMissingCandidateInstrumentCodes(List<InstrumentCode> instrumentCodes) {
        Objects.requireNonNull(instrumentCodes, "instrumentCodes must not be null");

        List<String> requestedValues = instrumentCodes.stream()
                .map(instrumentCode -> Objects.requireNonNull(instrumentCode, "instrumentCode must not be null"))
                .map(InstrumentCode::value)
                .toList();

        if (requestedValues.isEmpty()) {
            return List.of();
        }

        Set<String> existingValues = new HashSet<>(dsl.select(SOURCE_PLAN_INSTRUMENT_CODE)
                .from(SOURCE_PLAN)
                .where(SOURCE_PLAN_CAPTURER_CODE.eq(QuoteMonitorCapturer.CAPTURER_CODE.value()))
                .and(SOURCE_PLAN_EXECUTION_STATUS.eq(
                        StoredSourcePlanExecutionStatus.AVAILABLE.name()))
                .and(SOURCE_PLAN_INSTRUMENT_CODE.in(requestedValues))
                .fetch(SOURCE_PLAN_INSTRUMENT_CODE));

        return instrumentCodes.stream()
                .filter(instrumentCode -> !existingValues.contains(instrumentCode.value()))
                .toList();
    }
}
