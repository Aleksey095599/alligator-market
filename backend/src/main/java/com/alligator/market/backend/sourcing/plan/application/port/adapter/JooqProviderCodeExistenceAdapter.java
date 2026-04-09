package com.alligator.market.backend.sourcing.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

/**
 * jOOQ-адаптер порта проверки существования провайдера.
 */
public final class JooqProviderCodeExistenceAdapter implements ProviderCodeExistencePort {

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
        );
    }
}
