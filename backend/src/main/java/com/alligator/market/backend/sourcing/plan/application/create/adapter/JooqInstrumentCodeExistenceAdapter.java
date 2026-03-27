package com.alligator.market.backend.sourcing.plan.application.create.adapter;

import com.alligator.market.backend.sourcing.plan.application.create.port.InstrumentCodeExistencePort;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentBase.INSTRUMENT_BASE;

/**
 * jOOQ-адаптер порта проверки существования инструмента.
 */
public final class JooqInstrumentCodeExistenceAdapter implements InstrumentCodeExistencePort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqInstrumentCodeExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(INSTRUMENT_BASE)
                        .where(INSTRUMENT_BASE.CODE.eq(instrumentCode.value()))
        );
    }
}
