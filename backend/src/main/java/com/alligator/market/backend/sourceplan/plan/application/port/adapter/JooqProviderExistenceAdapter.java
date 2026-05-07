package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.ProviderExistencePort;
import com.alligator.market.domain.source.vo.ProviderCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

/**
 * jOOQ-адаптер {@link ProviderExistencePort}.
 */
public final class JooqProviderExistenceAdapter implements ProviderExistencePort {

    private final DSLContext dsl;

    public JooqProviderExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(ProviderCode providerCode) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(PROVIDER_PASSPORT)
                        .where(PROVIDER_PASSPORT.PROVIDER_CODE.eq(providerCode.value()))
                        .and(PROVIDER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );
    }
}
