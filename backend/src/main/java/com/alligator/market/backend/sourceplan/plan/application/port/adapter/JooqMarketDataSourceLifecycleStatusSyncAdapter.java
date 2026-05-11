package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.capturer.passport.persistence.projection.model.MarketDataCapturerProjectionLifecycleStatus;
import com.alligator.market.backend.source.passport.persistence.projection.model.MarketDataSourceProjectionLifecycleStatus;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.backend.sourceplan.plan.persistence.model.SourcePlanEntryLifecycleStatus;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;

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
                        .and(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(
                                MarketDataSourceProjectionLifecycleStatus.ACTIVE.name()))
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
                        .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(
                                MarketDataCapturerProjectionLifecycleStatus.ACTIVE.name()))
        );

        retireActiveSources(capturerIsNotActive);
    }

    private void retireActiveSources(Condition invalidReference) {
        dsl.update(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, SourcePlanEntryLifecycleStatus.RETIRED.name())
                .where(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(SourcePlanEntryLifecycleStatus.ACTIVE.name()))
                .and(invalidReference)
                .execute();
    }
}
