package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataSourceOptionsQueryPort;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

/**
 * jOOQ-адаптер порта получения доступных кодов провайдеров.
 */
public final class JooqMarketDataSourceOptionsQueryAdapter implements MarketDataSourceOptionsQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqMarketDataSourceOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataSourceCode> findAllSourceCodes() {
        return dsl.select(PROVIDER_PASSPORT.PROVIDER_CODE)
                .from(PROVIDER_PASSPORT)
                .where(PROVIDER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .orderBy(PROVIDER_PASSPORT.PROVIDER_CODE.asc())
                .fetch(PROVIDER_PASSPORT.PROVIDER_CODE)
                .stream()
                .map(MarketDataSourceCode::new)
                .toList();
    }
}
