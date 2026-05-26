package com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqQuoteMonitorInstrumentSelectionRepository
        implements QuoteMonitorInstrumentSelectionRepository {
    private static final Table<?> QUOTE_MONITOR_INSTRUMENTS = table(name("quote_monitor_instruments"));
    private static final Field<String> QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE =
            field(name("quote_monitor_instruments", "capturer_code"), String.class);
    private static final Field<String> QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE =
            field(name("quote_monitor_instruments", "instrument_code"), String.class);

    private final DSLContext dsl;

    public JooqQuoteMonitorInstrumentSelectionRepository(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public QuoteMonitorInstrumentSelection get() {
        return new QuoteMonitorInstrumentSelection(dsl.select(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE)
                .from(QUOTE_MONITOR_INSTRUMENTS)
                .where(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE.eq(QuoteMonitorCapturer.CAPTURER_CODE.value()))
                .orderBy(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE.asc())
                .fetch(record -> new InstrumentCode(record.get(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE))));
    }

    @Override
    public boolean addIfAbsent(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        int insertedRows = dsl.insertInto(QUOTE_MONITOR_INSTRUMENTS)
                .set(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE, instrumentCode.value())
                .set(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE, QuoteMonitorCapturer.CAPTURER_CODE.value())
                .onConflict(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE)
                .doNothing()
                .execute();

        return insertedRows > 0;
    }

    @Override
    public boolean removeIfExists(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        int deletedRows = dsl.deleteFrom(QUOTE_MONITOR_INSTRUMENTS)
                .where(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE.eq(QuoteMonitorCapturer.CAPTURER_CODE.value()))
                .and(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE.eq(instrumentCode.value()))
                .execute();

        return deletedRows > 0;
    }

    @Override
    public void replace(QuoteMonitorInstrumentSelection selection) {
        Objects.requireNonNull(selection, "selection must not be null");

        dsl.transaction(configuration -> {
            DSLContext tx = configuration.dsl();

            tx.deleteFrom(QUOTE_MONITOR_INSTRUMENTS)
                    .where(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE.eq(QuoteMonitorCapturer.CAPTURER_CODE.value()))
                    .execute();

            for (InstrumentCode instrumentCode : selection.instrumentCodes()) {
                tx.insertInto(QUOTE_MONITOR_INSTRUMENTS)
                        .set(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE, instrumentCode.value())
                        .set(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE, QuoteMonitorCapturer.CAPTURER_CODE.value())
                        .execute();
            }
        });
    }
}
