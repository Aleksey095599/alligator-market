package com.alligator.market.backend.provider.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.provider.passport.application.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;
import static org.jooq.impl.DSL.excluded;

/**
 * jOOQ implementation of {@link ProviderPassportProjectionWritePort}.
 */
public class JooqProviderPassportProjectionWritePortAdapter implements ProviderPassportProjectionWritePort {

    private final DSLContext dsl;

    public JooqProviderPassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireAllExcept(Set<ProviderCode> currentCodes) {
        validateCurrentCodes(currentCodes);

        Set<String> currentValues = new LinkedHashSet<>(currentCodes.size());
        for (ProviderCode code : currentCodes) {
            currentValues.add(code.value());
        }

        dsl.update(PROVIDER_PASSPORT)
                .set(PROVIDER_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(PROVIDER_PASSPORT.PROVIDER_CODE.notIn(currentValues))
                .and(PROVIDER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void upsertAll(Map<ProviderCode, ProviderPassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return;
        }

        List<Map.Entry<ProviderCode, ProviderPassport>> entries = toValidatedEntries(passports);
        List<Query> queries = new ArrayList<>(entries.size());

        for (Map.Entry<ProviderCode, ProviderPassport> entry : entries) {
            ProviderCode code = entry.getKey();
            ProviderPassport passport = entry.getValue();

            Condition businessFieldsChanged = PROVIDER_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(PROVIDER_PASSPORT.DISPLAY_NAME))
                    .or(PROVIDER_PASSPORT.DELIVERY_MODE.isDistinctFrom(excluded(PROVIDER_PASSPORT.DELIVERY_MODE)))
                    .or(PROVIDER_PASSPORT.ACCESS_METHOD.isDistinctFrom(excluded(PROVIDER_PASSPORT.ACCESS_METHOD)))
                    .or(PROVIDER_PASSPORT.BULK_SUBSCRIPTION.isDistinctFrom(excluded(PROVIDER_PASSPORT.BULK_SUBSCRIPTION)))
                    .or(PROVIDER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(ACTIVE.name()));

            Query query = dsl.insertInto(PROVIDER_PASSPORT)
                    .set(PROVIDER_PASSPORT.PROVIDER_CODE, code.value())
                    .set(PROVIDER_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(PROVIDER_PASSPORT.DELIVERY_MODE, passport.deliveryMode().name())
                    .set(PROVIDER_PASSPORT.ACCESS_METHOD, passport.accessMethod().name())
                    .set(PROVIDER_PASSPORT.BULK_SUBSCRIPTION, passport.bulkSubscription())
                    .set(PROVIDER_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .onConflict(PROVIDER_PASSPORT.PROVIDER_CODE)
                    .doUpdate()
                    .set(PROVIDER_PASSPORT.DISPLAY_NAME, excluded(PROVIDER_PASSPORT.DISPLAY_NAME))
                    .set(PROVIDER_PASSPORT.DELIVERY_MODE, excluded(PROVIDER_PASSPORT.DELIVERY_MODE))
                    .set(PROVIDER_PASSPORT.ACCESS_METHOD, excluded(PROVIDER_PASSPORT.ACCESS_METHOD))
                    .set(PROVIDER_PASSPORT.BULK_SUBSCRIPTION, excluded(PROVIDER_PASSPORT.BULK_SUBSCRIPTION))
                    .set(PROVIDER_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateCurrentCodes(Set<ProviderCode> currentCodes) {
        if (currentCodes == null) {
            throw new IllegalArgumentException("currentCodes must not be null");
        }
        if (currentCodes.isEmpty()) {
            throw new IllegalArgumentException("currentCodes must not be empty");
        }

        for (ProviderCode code : currentCodes) {
            if (code == null) {
                throw new IllegalArgumentException("currentCodes must not contain null");
            }
        }
    }

    private static void validatePassportsMap(Map<ProviderCode, ProviderPassport> passports) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }
    }

    private static List<Map.Entry<ProviderCode, ProviderPassport>> toValidatedEntries(
            Map<ProviderCode, ProviderPassport> passports
    ) {
        List<Map.Entry<ProviderCode, ProviderPassport>> entries = new ArrayList<>(passports.size());
        for (Map.Entry<ProviderCode, ProviderPassport> entry : passports.entrySet()) {
            if (entry.getKey() == null) {
                throw new IllegalArgumentException("passports must not contain null keys");
            }
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("passports must not contain null values");
            }
            entries.add(entry);
        }
        return entries;
    }
}
