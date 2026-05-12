package com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.LiveQuoteMonitorCapturer;
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

public final class JooqQuoteMonitorInstrumentAdapter implements QuoteMonitorInstrumentPort {
    private static final Table<?> SOURCE_PLAN = table(name("source_plan"));
    private static final Table<?> QUOTE_MONITOR_INSTRUMENTS = table(name("quote_monitor_instruments"));
    private static final Field<String> SOURCE_PLAN_CAPTURER_CODE =
            field(name("source_plan", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_INSTRUMENT_CODE =
            field(name("source_plan", "instrument_code"), String.class);
    private static final Field<String> QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE =
            field(name("quote_monitor_instruments", "capturer_code"), String.class);
    private static final Field<String> QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE =
            field(name("quote_monitor_instruments", "instrument_code"), String.class);

    private final DSLContext dsl;

    public JooqQuoteMonitorInstrumentAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<InstrumentCode> findAvailableInstrumentCodes() {
        return dsl.select(SOURCE_PLAN_INSTRUMENT_CODE)
                .from(SOURCE_PLAN)
                .where(SOURCE_PLAN_CAPTURER_CODE.eq(LiveQuoteMonitorCapturer.CAPTURER_CODE.value()))
                .orderBy(SOURCE_PLAN_INSTRUMENT_CODE.asc())
                .fetch(record -> new InstrumentCode(record.get(SOURCE_PLAN_INSTRUMENT_CODE)));
    }

    @Override
    public List<InstrumentCode> findSelectedInstrumentCodes() {
        return dsl.select(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE)
                .from(QUOTE_MONITOR_INSTRUMENTS)
                .orderBy(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE.asc())
                .fetch(record -> new InstrumentCode(record.get(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE)));
    }

    @Override
    public List<InstrumentCode> findInstrumentCodesWithoutSourcePlan(List<InstrumentCode> instrumentCodes) {
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
                .where(SOURCE_PLAN_CAPTURER_CODE.eq(LiveQuoteMonitorCapturer.CAPTURER_CODE.value()))
                .and(SOURCE_PLAN_INSTRUMENT_CODE.in(requestedValues))
                .fetch(SOURCE_PLAN_INSTRUMENT_CODE));

        return instrumentCodes.stream()
                .filter(instrumentCode -> !existingValues.contains(instrumentCode.value()))
                .toList();
    }

    @Override
    public void replaceSelectedInstrumentCodes(List<InstrumentCode> instrumentCodes) {
        Objects.requireNonNull(instrumentCodes, "instrumentCodes must not be null");

        dsl.transaction(configuration -> {
            DSLContext tx = configuration.dsl();

            tx.deleteFrom(QUOTE_MONITOR_INSTRUMENTS)
                    .execute();

            for (InstrumentCode instrumentCode : instrumentCodes) {
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

                tx.insertInto(QUOTE_MONITOR_INSTRUMENTS)
                        .set(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE, instrumentCode.value())
                        .set(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE, LiveQuoteMonitorCapturer.CAPTURER_CODE.value())
                        .execute();
            }
        });
    }
}
