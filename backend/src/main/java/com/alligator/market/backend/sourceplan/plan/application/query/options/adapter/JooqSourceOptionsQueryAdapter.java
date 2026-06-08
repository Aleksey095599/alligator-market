package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.port.SourceOptionsQueryPort;
import com.alligator.market.domain.source.vo.SourceCode;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static com.alligator.market.domain.source.passport.store.SourcePassportRecord.RegistryStatus.REGISTERED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public final class JooqSourceOptionsQueryAdapter implements SourceOptionsQueryPort {
    private static final Field<String> SOURCE_PASSPORT_REGISTRY_STATUS =
            field(name("source_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqSourceOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<SourceCode> findAllSourceCodes() {
        return dsl.select(SOURCE_PASSPORT.SOURCE_CODE)
                .from(SOURCE_PASSPORT)
                .where(SOURCE_PASSPORT_REGISTRY_STATUS.eq(REGISTERED.name()))
                .orderBy(SOURCE_PASSPORT.SOURCE_CODE.asc())
                .fetch(SOURCE_PASSPORT.SOURCE_CODE)
                .stream()
                .map(SourceCode::new)
                .toList();
    }
}
