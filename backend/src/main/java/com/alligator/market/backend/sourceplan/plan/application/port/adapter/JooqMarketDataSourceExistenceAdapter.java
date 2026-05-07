package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceExistencePort;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;

/**
 * jOOQ adapter for checking active market data source passports.
 */
public final class JooqMarketDataSourceExistenceAdapter implements MarketDataSourceExistencePort {

    private final DSLContext dsl;

    public JooqMarketDataSourceExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(MarketDataSourceCode sourceCode) {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(MARKET_DATA_SOURCE_PASSPORT)
                        .where(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE.eq(sourceCode.value()))
                        .and(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );
    }
}
