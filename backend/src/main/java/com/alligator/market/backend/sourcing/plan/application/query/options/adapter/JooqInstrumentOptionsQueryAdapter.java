package com.alligator.market.backend.sourcing.plan.application.query.options.adapter;

import com.alligator.market.backend.sourcing.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentRegistry.INSTRUMENT_REGISTRY;

/**
 * jOOQ-адаптер порта получения доступных кодов инструментов.
 */
public final class JooqInstrumentOptionsQueryAdapter implements InstrumentOptionsQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqInstrumentOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<InstrumentCode> findAllInstrumentCodes() {
        return dsl.select(INSTRUMENT_REGISTRY.CODE)
                .from(INSTRUMENT_REGISTRY)
                .orderBy(INSTRUMENT_REGISTRY.CODE.asc())
                .fetch(INSTRUMENT_REGISTRY.CODE)
                .stream()
                .map(InstrumentCode::new)
                .toList();
    }
}
