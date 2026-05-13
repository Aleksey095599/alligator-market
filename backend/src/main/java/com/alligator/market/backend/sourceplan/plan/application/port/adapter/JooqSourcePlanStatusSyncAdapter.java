package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.source.passport.persistence.projection.model.StoredSourceProjectionLifecycleStatus;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.backend.sourceplan.plan.persistence.model.StoredSourcePlanEntryLifecycleStatus;
import com.alligator.market.backend.sourceplan.plan.persistence.model.StoredSourcePlanExecutionStatus;
import com.alligator.market.domain.capturer.passport.registry.StoredCapturerPassportRegistryStatus;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;

public final class JooqSourcePlanStatusSyncAdapter
        implements SourcePlanStatusSyncPort {
    private static final Table<?> SOURCE_PLAN = table(name("source_plan"));
    private static final Table<?> SOURCE_PLAN_ENTRY = table(name("source_plan_entry"));
    private static final Field<String> SOURCE_PLAN_CAPTURER_CODE =
            field(name("source_plan", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_INSTRUMENT_CODE =
            field(name("source_plan", "instrument_code"), String.class);
    private static final Field<String> SOURCE_PLAN_EXECUTION_STATUS =
            field(name("source_plan", "execution_status"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_CAPTURER_CODE =
            field(name("source_plan_entry", "capturer_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_INSTRUMENT_CODE =
            field(name("source_plan_entry", "instrument_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_SOURCE_CODE =
            field(name("source_plan_entry", "source_code"), String.class);
    private static final Field<String> SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS =
            field(name("source_plan_entry", "lifecycle_status"), String.class);

    private final DSLContext dsl;

    public JooqSourcePlanStatusSyncAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void syncSourcePlanStatuses() {
        syncSourcePlanEntryLifecycleStatuses();
        refreshPlanExecutionStatuses();
    }

    private void syncSourcePlanEntryLifecycleStatuses() {
        Condition activeReferences = activeSourcePassportExistsForEntry()
                .and(activeCapturerPassportExistsForEntry());

        dsl.update(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, StoredSourcePlanEntryLifecycleStatus.ACTIVE.name())
                .where(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS
                        .isDistinctFrom(StoredSourcePlanEntryLifecycleStatus.ACTIVE.name()))
                .and(activeReferences)
                .execute();

        dsl.update(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, StoredSourcePlanEntryLifecycleStatus.RETIRED.name())
                .where(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS
                        .isDistinctFrom(StoredSourcePlanEntryLifecycleStatus.RETIRED.name()))
                .and(activeReferences.not())
                .execute();
    }

    private Condition activeSourcePassportExistsForEntry() {
        return exists(
                selectOne()
                        .from(SOURCE_PASSPORT)
                        .where(SOURCE_PASSPORT.SOURCE_CODE
                                .eq(SOURCE_PLAN_ENTRY_SOURCE_CODE))
                        .and(SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(
                                StoredSourceProjectionLifecycleStatus.ACTIVE.name()))
        );
    }

    private Condition activeCapturerPassportExistsForEntry() {
        return exists(
                selectOne()
                        .from(MARKET_DATA_CAPTURER_PASSPORT)
                        .where(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE
                                .eq(SOURCE_PLAN_ENTRY_CAPTURER_CODE))
                        .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(
                                StoredCapturerPassportRegistryStatus.ACTIVE.name()))
        );
    }

    private void refreshPlanExecutionStatuses() {
        Condition capturerIsNotActive = notExists(
                selectOne()
                        .from(MARKET_DATA_CAPTURER_PASSPORT)
                        .where(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                        .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.eq(
                                StoredCapturerPassportRegistryStatus.ACTIVE.name()))
        );

        Condition hasActiveSources = exists(
                selectOne()
                        .from(SOURCE_PLAN_ENTRY)
                        .join(SOURCE_PASSPORT)
                        .on(SOURCE_PASSPORT.SOURCE_CODE.eq(SOURCE_PLAN_ENTRY_SOURCE_CODE))
                        .where(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                        .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                        .and(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(StoredSourcePlanEntryLifecycleStatus.ACTIVE.name()))
                        .and(SOURCE_PASSPORT.LIFECYCLE_STATUS.eq(
                                StoredSourceProjectionLifecycleStatus.ACTIVE.name()))
        );

        dsl.update(SOURCE_PLAN)
                .set(SOURCE_PLAN_EXECUTION_STATUS, StoredSourcePlanExecutionStatus.CAPTURER_RETIRED.name())
                .where(capturerIsNotActive)
                .execute();

        dsl.update(SOURCE_PLAN)
                .set(SOURCE_PLAN_EXECUTION_STATUS, StoredSourcePlanExecutionStatus.NO_EXECUTABLE_SOURCES.name())
                .where(capturerIsNotActive.not())
                .and(hasActiveSources.not())
                .execute();

        dsl.update(SOURCE_PLAN)
                .set(SOURCE_PLAN_EXECUTION_STATUS, StoredSourcePlanExecutionStatus.EXECUTABLE.name())
                .where(capturerIsNotActive.not())
                .and(hasActiveSources)
                .execute();
    }
}
