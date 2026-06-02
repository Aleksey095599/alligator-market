package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.SourceExistencePort;
import com.alligator.market.domain.source.vo.SourceCode;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport.RegistryStatus.REGISTERED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public final class JooqSourceExistenceAdapter implements SourceExistencePort {
    private static final Field<String> SOURCE_PASSPORT_REGISTRY_STATUS =
            field(name("source_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqSourceExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(SourceCode sourceCode) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(SOURCE_PASSPORT)
                        .where(SOURCE_PASSPORT.SOURCE_CODE.eq(sourceCode.value()))
                        .and(SOURCE_PASSPORT_REGISTRY_STATUS.eq(REGISTERED.name()))
        );
    }
}
