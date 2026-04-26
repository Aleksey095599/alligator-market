package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.adapter;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.port.SourcePlanExistenceQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePlan.SOURCE_PLAN;

/**
 * jOOQ-адаптер existence-проверки source plan.
 */
public final class JooqSourcePlanExistenceQueryAdapter implements SourcePlanExistenceQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqSourcePlanExistenceQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(SOURCE_PLAN)
                        .where(SOURCE_PLAN.INSTRUMENT_CODE.eq(instrumentCode.value()))
        );
    }
}
