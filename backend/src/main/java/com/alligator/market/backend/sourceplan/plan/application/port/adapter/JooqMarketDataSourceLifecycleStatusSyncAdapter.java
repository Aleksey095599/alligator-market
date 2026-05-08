package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePlanEntry.MARKET_DATA_SOURCE_PLAN_ENTRY;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;

/**
 * jOOQ implementation of {@link MarketDataSourceLifecycleStatusSyncPort}.
 *
 * <p>The synchronization is monotonic: it only moves ACTIVE source plan entries to RETIRED and
 * never promotes RETIRED entries back to ACTIVE. If a source is needed again, the plan entry should
 * be deleted and added again.</p>
 */
public final class JooqMarketDataSourceLifecycleStatusSyncAdapter
        implements MarketDataSourceLifecycleStatusSyncPort {

    private final DSLContext dsl;

    public JooqMarketDataSourceLifecycleStatusSyncAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireSourcesWithoutActiveSourcePassports() {
        Condition sourcePassportIsNotActive = notExists(
                selectOne()
                        .from(MARKET_DATA_SOURCE_PASSPORT)
                        .where(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE
                                .eq(MARKET_DATA_SOURCE_PLAN_ENTRY.SOURCE_CODE))
                        .and(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(sourcePassportIsNotActive);
    }

    @Override
    public void retireSourcesWithoutActiveMarketDataCapturerPassports() {
        Condition capturerIsNotActive = notExists(
                selectOne()
                        .from(MARKET_DATA_CAPTURER_PASSPORT)
                        .where(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE
                                .eq(MARKET_DATA_SOURCE_PLAN_ENTRY.CAPTURER_CODE))
                        .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(capturerIsNotActive);
    }

    private void retireActiveSources(Condition invalidReference) {
        // RETIRED is terminal here; only still-active plan entries are changed.
        dsl.update(MARKET_DATA_SOURCE_PLAN_ENTRY)
                .set(MARKET_DATA_SOURCE_PLAN_ENTRY.LIFECYCLE_STATUS, RETIRED.name())
                .where(MARKET_DATA_SOURCE_PLAN_ENTRY.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .and(invalidReference)
                .execute();
    }
}
