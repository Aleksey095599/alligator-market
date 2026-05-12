package com.alligator.market.backend.source.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.source.passport.application.projection.port.SourcePassportProjectionWritePort;
import com.alligator.market.backend.source.passport.persistence.projection.mapper.StoredSourcePassportMapper;
import com.alligator.market.backend.source.passport.persistence.projection.model.StoredSourcePassport;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.backend.source.passport.persistence.projection.model.StoredSourceProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static org.jooq.impl.DSL.excluded;

public class JooqSourcePassportProjectionWritePortAdapter implements SourcePassportProjectionWritePort {
    private final DSLContext dsl;
    private final StoredSourcePassportMapper storedPassportMapper;

    public JooqSourcePassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
        this.storedPassportMapper = new StoredSourcePassportMapper();
    }

    @Override
    public void retireAllExcept(Set<SourceCode> passportCodes) {
        validateCurrentCodes(passportCodes);

        Set<String> currentValues = new LinkedHashSet<>(passportCodes.size());
        for (SourceCode code : passportCodes) {
            currentValues.add(code.value());
        }

        dsl.update(SOURCE_PASSPORT)
                .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(SOURCE_PASSPORT.SOURCE_CODE.notIn(currentValues))
                .and(SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void upsertAll(Map<SourceCode, SourcePassport> passports) {
        List<StoredSourcePassport> storedPassports = storedPassportMapper.toActiveStored(passports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (StoredSourcePassport storedPassport : storedPassports) {
            SourceCode code = storedPassport.sourceCode();
            SourcePassport passport = storedPassport.passport();
            String lifecycleStatus = storedPassport.lifecycleStatus().name();

            Condition businessFieldsChanged = SOURCE_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(SOURCE_PASSPORT.DISPLAY_NAME))
                    .or(SOURCE_PASSPORT.DELIVERY_MODE
                            .isDistinctFrom(excluded(SOURCE_PASSPORT.DELIVERY_MODE)))
                    .or(SOURCE_PASSPORT.ACCESS_METHOD
                            .isDistinctFrom(excluded(SOURCE_PASSPORT.ACCESS_METHOD)))
                    .or(SOURCE_PASSPORT.BULK_SUBSCRIPTION
                            .isDistinctFrom(excluded(SOURCE_PASSPORT.BULK_SUBSCRIPTION)))
                    .or(SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(lifecycleStatus));

            Query query = dsl.insertInto(SOURCE_PASSPORT)
                    .set(SOURCE_PASSPORT.SOURCE_CODE, code.value())
                    .set(SOURCE_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(SOURCE_PASSPORT.DELIVERY_MODE, passport.deliveryMode().name())
                    .set(SOURCE_PASSPORT.ACCESS_METHOD, passport.accessMethod().name())
                    .set(SOURCE_PASSPORT.BULK_SUBSCRIPTION, passport.bulkSubscription())
                    .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, lifecycleStatus)
                    .onConflict(SOURCE_PASSPORT.SOURCE_CODE)
                    .doUpdate()
                    .set(SOURCE_PASSPORT.DISPLAY_NAME,
                            excluded(SOURCE_PASSPORT.DISPLAY_NAME))
                    .set(SOURCE_PASSPORT.DELIVERY_MODE,
                            excluded(SOURCE_PASSPORT.DELIVERY_MODE))
                    .set(SOURCE_PASSPORT.ACCESS_METHOD,
                            excluded(SOURCE_PASSPORT.ACCESS_METHOD))
                    .set(SOURCE_PASSPORT.BULK_SUBSCRIPTION,
                            excluded(SOURCE_PASSPORT.BULK_SUBSCRIPTION))
                    .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, lifecycleStatus)
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateCurrentCodes(Set<SourceCode> currentCodes) {
        if (currentCodes == null) {
            throw new IllegalArgumentException("currentCodes must not be null");
        }
        if (currentCodes.isEmpty()) {
            throw new IllegalArgumentException("currentCodes must not be empty");
        }

        for (SourceCode code : currentCodes) {
            if (code == null) {
                throw new IllegalArgumentException("currentCodes must not contain null");
            }
        }
    }
}
