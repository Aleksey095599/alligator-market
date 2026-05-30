package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.CapturerExistencePort;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
import static com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassport.Status.REGISTERED;

public final class JooqCapturerExistenceAdapter implements CapturerExistencePort {
    private final DSLContext dsl;

    public JooqCapturerExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(CapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(CAPTURER_PASSPORT)
                        .where(CAPTURER_PASSPORT.CAPTURER_CODE.eq(capturerCode.value()))
                        .and(CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(REGISTERED.name()))
        );
    }
}
