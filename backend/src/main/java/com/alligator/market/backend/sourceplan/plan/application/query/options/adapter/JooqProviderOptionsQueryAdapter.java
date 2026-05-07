package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.port.ProviderOptionsQueryPort;
import com.alligator.market.domain.source.vo.ProviderCode;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

/**
 * jOOQ-адаптер порта получения доступных кодов провайдеров.
 */
public final class JooqProviderOptionsQueryAdapter implements ProviderOptionsQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqProviderOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<ProviderCode> findAllProviderCodes() {
        return dsl.select(PROVIDER_PASSPORT.PROVIDER_CODE)
                .from(PROVIDER_PASSPORT)
                .where(PROVIDER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .orderBy(PROVIDER_PASSPORT.PROVIDER_CODE.asc())
                .fetch(PROVIDER_PASSPORT.PROVIDER_CODE)
                .stream()
                .map(ProviderCode::new)
                .toList();
    }
}
