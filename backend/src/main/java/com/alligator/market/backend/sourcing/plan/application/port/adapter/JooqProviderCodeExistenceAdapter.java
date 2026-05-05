package com.alligator.market.backend.sourcing.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

/**
 * jOOQ-адаптер порта проверки существования провайдера.
 */
public final class JooqProviderCodeExistenceAdapter implements ProviderCodeExistencePort {

    private static final Field<String> LIFECYCLE_STATUS = field(name("lifecycle_status"), String.class);

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqProviderCodeExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(ProviderCode providerCode) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(PROVIDER_PASSPORT)
                        .where(PROVIDER_PASSPORT.PROVIDER_CODE.eq(providerCode.value()))
                        .and(LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );
    }
}
