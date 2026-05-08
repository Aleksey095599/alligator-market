package com.alligator.market.backend.sourceplan.plan.application.query.existence.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.existence.port.SourcePlanExistenceQueryPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public final class JooqSourcePlanExistenceQueryAdapter implements SourcePlanExistenceQueryPort {
    private static final Table<?> SOURCE_PLAN = table(name("source_plan"));
    private static final Field<String> SOURCE_PLAN_INSTRUMENT_CODE =
            field(name("source_plan", "instrument_code"), String.class);

    private final DSLContext dsl;

    public JooqSourcePlanExistenceQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(SOURCE_PLAN)
                        .where(SOURCE_PLAN_INSTRUMENT_CODE.eq(instrumentCode.value()))
        );
    }
}
