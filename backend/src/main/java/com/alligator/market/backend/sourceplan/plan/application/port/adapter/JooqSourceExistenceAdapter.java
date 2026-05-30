package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.SourceExistencePort;
import com.alligator.market.domain.source.vo.SourceCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport.Status.REGISTERED;

public final class JooqSourceExistenceAdapter implements SourceExistencePort {
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
                        .and(SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(REGISTERED.name()))
        );
    }
}
