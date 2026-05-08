package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataCapturerExistencePort;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;

/**
 * jOOQ-адаптер {@link MarketDataCapturerExistencePort}.
 */
public final class JooqMarketDataCapturerExistenceAdapter implements MarketDataCapturerExistencePort {

    private final DSLContext dsl;

    public JooqMarketDataCapturerExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(MarketDataCapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(MARKET_DATA_CAPTURER_PASSPORT)
                        .where(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.eq(capturerCode.value()))
                        .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );
    }
}
