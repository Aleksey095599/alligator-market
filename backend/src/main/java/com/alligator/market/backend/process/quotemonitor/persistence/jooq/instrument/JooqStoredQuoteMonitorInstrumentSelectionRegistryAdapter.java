package com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.LiveQuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.stored.StoredQuoteMonitorInstrumentSelectionRegistry;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqStoredQuoteMonitorInstrumentSelectionRegistryAdapter
        implements StoredQuoteMonitorInstrumentSelectionRegistry {
    private static final Table<?> QUOTE_MONITOR_INSTRUMENTS = table(name("quote_monitor_instruments"));
    private static final Field<String> QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE =
            field(name("quote_monitor_instruments", "capturer_code"), String.class);
    private static final Field<String> QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE =
            field(name("quote_monitor_instruments", "instrument_code"), String.class);

    private final DSLContext dsl;

    public JooqStoredQuoteMonitorInstrumentSelectionRegistryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public QuoteMonitorInstrumentSelection getSelection() {
        return new QuoteMonitorInstrumentSelection(dsl.select(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE)
                .from(QUOTE_MONITOR_INSTRUMENTS)
                .where(QUOTE_MONITOR_INSTRUMENTS_CAPTURER_CODE.eq(LiveQuoteMonitorCapturer.CAPTURER_CODE.value()))
                .orderBy(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE.asc())
                .fetch(record -> new InstrumentCode(record.get(QUOTE_MONITOR_INSTRUMENTS_INSTRUMENT_CODE))));
    }
}
