package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;

/**
 * jOOQ implementation of {@link MarketDataSourceLifecycleStatusSyncPort}.
 *
 * <p>The synchronization is monotonic: it only moves ACTIVE source plan entries to RETIRED and
 * never promotes RETIRED entries back to ACTIVE. If a source is needed again, the plan entry should
 * be deleted and added again.</p>
 */
public final class JooqMarketDataSourceLifecycleStatusSyncAdapter
        implements MarketDataSourceLifecycleStatusSyncPort {

    private static final Table<?> SOURCE_PLAN_ENTRY = table(name("source_plan_entry"));
    private static final Field<String> SOURCE_PLAN_ENTRY_CAPTURER_CODE =
            field(name("source_plan_entry", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_SOURCE_CODE =
            field(name("source_plan_entry", "source_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS =
            field(name("source_plan_entry", "lifecycle_status"), String.class);

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
                                .eq(SOURCE_PLAN_ENTRY_SOURCE_CODE))
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
                                .eq(SOURCE_PLAN_ENTRY_CAPTURER_CODE))
                        .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );

        retireActiveSources(capturerIsNotActive);
    }

    private void retireActiveSources(Condition invalidReference) {
        // RETIRED is terminal here; only still-active plan entries are changed.
        dsl.update(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, RETIRED.name())
                .where(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(ACTIVE.name()))
                .and(invalidReference)
                .execute();
    }
}
