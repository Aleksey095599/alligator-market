package com.alligator.market.backend.sourcing.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentRegistry.INSTRUMENT_REGISTRY;

/**
 * jOOQ-адаптер {@link InstrumentCodeExistencePort}.
 */
public final class JooqInstrumentCodeExistenceAdapter implements InstrumentCodeExistencePort {

    private final DSLContext dsl;

    public JooqInstrumentCodeExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(INSTRUMENT_REGISTRY)
                        .where(INSTRUMENT_REGISTRY.CODE.eq(instrumentCode.value()))
        );
    }
}
