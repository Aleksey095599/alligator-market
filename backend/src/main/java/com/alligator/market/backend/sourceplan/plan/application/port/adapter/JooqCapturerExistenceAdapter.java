package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.CapturerExistencePort;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
import static com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord.RegistryStatus.REGISTERED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public final class JooqCapturerExistenceAdapter implements CapturerExistencePort {
    private static final Field<String> CAPTURER_PASSPORT_REGISTRY_STATUS =
            field(name("capturer_passport", "registry_status"), String.class);

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
                        .and(CAPTURER_PASSPORT_REGISTRY_STATUS.eq(REGISTERED.name()))
        );
    }
}
