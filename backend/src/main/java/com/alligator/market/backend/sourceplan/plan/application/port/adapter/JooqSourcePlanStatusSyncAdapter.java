package com.alligator.market.backend.sourceplan.plan.application.port.adapter;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlan;
import com.alligator.market.domain.sourceplan.registry.stored.StoredSourcePlanStatusPolicy;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
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
    private static final Field<String> SOURCE_PASSPORT_REGISTRY_STATUS =
            field(name("source_passport", "registry_status"), String.class);
    private static final Field<String> CAPTURER_PASSPORT_REGISTRY_STATUS =
            field(name("capturer_passport", "registry_status"), String.class);

    private final DSLContext dsl;
    private final StoredSourcePlanStatusPolicy statusPolicy;

    public JooqSourcePlanStatusSyncAdapter(
            DSLContext dsl,
            StoredSourcePlanStatusPolicy statusPolicy
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
        this.statusPolicy = Objects.requireNonNull(statusPolicy, "statusPolicy must not be null");
    }

    @Override
    public void syncSourcePlanStatuses() {
        syncSourcePlanEntryLifecycleStatuses();
        refreshPlanAvailabilityStatuses();
    }

    private void syncSourcePlanEntryLifecycleStatuses() {
        Condition registeredSourceReference = registeredSourcePassportExistsForEntry();
        String availableEntryStatus = statusPolicy.resolveEntryLifecycleStatus(true).name();
        String sourceRetiredEntryStatus = statusPolicy.resolveEntryLifecycleStatus(false).name();

        dsl.update(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, availableEntryStatus)
                .where(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS
                        .isDistinctFrom(availableEntryStatus))
                .and(registeredSourceReference)
                .execute();

        dsl.update(SOURCE_PLAN_ENTRY)
                .set(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS, sourceRetiredEntryStatus)
                .where(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS
                        .isDistinctFrom(sourceRetiredEntryStatus))
                .and(registeredSourceReference.not())
                .execute();
    }

    private Condition registeredSourcePassportExistsForEntry() {
        return exists(
                selectOne()
                        .from(SOURCE_PASSPORT)
                        .where(SOURCE_PASSPORT.SOURCE_CODE
                                .eq(SOURCE_PLAN_ENTRY_SOURCE_CODE))
                        .and(SOURCE_PASSPORT_REGISTRY_STATUS.eq(
                                StoredSourcePassport.RegistryStatus.REGISTERED.name()))
        );
    }

    private void refreshPlanAvailabilityStatuses() {
        String availableEntryStatus = StoredSourcePlan.EntryLifecycleStatus.AVAILABLE.name();
        String capturerRetiredPlanStatus = statusPolicy.resolvePlanExecutionStatus(false, false).name();
        String noAvailableSourcesPlanStatus = statusPolicy.resolvePlanExecutionStatus(true, false).name();
        String availablePlanStatus = statusPolicy.resolvePlanExecutionStatus(true, true).name();

        Condition capturerIsNotRegistered = notExists(
                selectOne()
                        .from(CAPTURER_PASSPORT)
                        .where(CAPTURER_PASSPORT.CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                        .and(CAPTURER_PASSPORT_REGISTRY_STATUS.eq(
                                CapturerPassportRecord.RegistryStatus.REGISTERED.name()))
        );

        Condition hasAvailableSources = exists(
                selectOne()
                        .from(SOURCE_PLAN_ENTRY)
                        .join(SOURCE_PASSPORT)
                        .on(SOURCE_PASSPORT.SOURCE_CODE.eq(SOURCE_PLAN_ENTRY_SOURCE_CODE))
                        .where(SOURCE_PLAN_ENTRY_CAPTURER_CODE.eq(SOURCE_PLAN_CAPTURER_CODE))
                        .and(SOURCE_PLAN_ENTRY_INSTRUMENT_CODE.eq(SOURCE_PLAN_INSTRUMENT_CODE))
                        .and(SOURCE_PLAN_ENTRY_LIFECYCLE_STATUS.eq(availableEntryStatus))
                        .and(SOURCE_PASSPORT_REGISTRY_STATUS.eq(
                                StoredSourcePassport.RegistryStatus.REGISTERED.name()))
        );

        dsl.update(SOURCE_PLAN)
                .set(SOURCE_PLAN_EXECUTION_STATUS, capturerRetiredPlanStatus)
                .where(capturerIsNotRegistered)
                .execute();

        dsl.update(SOURCE_PLAN)
                .set(SOURCE_PLAN_EXECUTION_STATUS, noAvailableSourcesPlanStatus)
                .where(capturerIsNotRegistered.not())
                .and(hasAvailableSources.not())
                .execute();

        dsl.update(SOURCE_PLAN)
                .set(SOURCE_PLAN_EXECUTION_STATUS, availablePlanStatus)
                .where(capturerIsNotRegistered.not())
                .and(hasAvailableSources)
                .execute();
    }
}
