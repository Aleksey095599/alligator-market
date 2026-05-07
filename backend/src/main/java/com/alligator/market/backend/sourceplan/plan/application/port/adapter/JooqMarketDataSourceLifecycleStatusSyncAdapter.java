package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSource.MARKET_DATA_SOURCE;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;

/**
 * jOOQ implementation of {@link MarketDataSourceLifecycleStatusSyncPort}.
 *
 * <p>The synchronization is monotonic: it only moves ACTIVE source rows to RETIRED and never
 * promotes RETIRED rows back to ACTIVE. If a source is needed again, the plan entry should be
 * deleted and added again.</p>
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
                        .where(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE.eq(MARKET_DATA_SOURCE.SOURCE_CODE))
                        .and(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(sourcePassportIsNotActive);
    }

    @Override
    public void retireSourcesWithoutActiveCaptureProcessPassports() {
        Condition captureProcessIsNotActive = notExists(
                selectOne()
                        .from(CAPTURE_PROCESS_PASSPORT)
                        .where(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE
                                .eq(MARKET_DATA_SOURCE.COLLECTION_PROCESS_CODE))
                        .and(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(captureProcessIsNotActive);
    }

    private void retireActiveSources(Condition invalidReference) {
        // RETIRED is terminal here; only still-active rows are changed.
        dsl.update(MARKET_DATA_SOURCE)
                .set(MARKET_DATA_SOURCE.LIFECYCLE_STATUS, RETIRED.name())
                .where(MARKET_DATA_SOURCE.LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .and(invalidReference)
                .execute();
    }
}
