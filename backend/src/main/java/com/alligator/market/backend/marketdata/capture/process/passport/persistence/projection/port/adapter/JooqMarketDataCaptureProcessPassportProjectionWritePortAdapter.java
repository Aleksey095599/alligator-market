package com.alligator.market.backend.marketdata.capture.process.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MarketDataCaptureProcessPassportProjectionWritePort;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
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
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;
import static org.jooq.impl.DSL.excluded;

/**
 * jOOQ implementation of {@link MarketDataCaptureProcessPassportProjectionWritePort}.
 */
public class JooqMarketDataCaptureProcessPassportProjectionWritePortAdapter
        implements MarketDataCaptureProcessPassportProjectionWritePort {

    private final DSLContext dsl;

    public JooqMarketDataCaptureProcessPassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireAllExcept(Set<MarketDataCaptureProcessCode> currentCodes) {
        validateCurrentCodes(currentCodes);

        Set<String> currentValues = new LinkedHashSet<>(currentCodes.size());
        for (MarketDataCaptureProcessCode code : currentCodes) {
            currentValues.add(code.value());
        }

        dsl.update(CAPTURE_PROCESS_PASSPORT)
                .set(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.notIn(currentValues))
                .and(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void upsertAll(Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return;
        }

        List<Map.Entry<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport>> entries = toValidatedEntries(passports);
        List<Query> queries = new ArrayList<>(entries.size());

        for (Map.Entry<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> entry : entries) {
            MarketDataCaptureProcessCode code = entry.getKey();
            MarketDataCaptureProcessPassport passport = entry.getValue();

            Condition businessFieldsChanged = CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME))
                    .or(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(ACTIVE.name()));

            Query query = dsl.insertInto(CAPTURE_PROCESS_PASSPORT)
                    .set(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE, code.value())
                    .set(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .onConflict(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE)
                    .doUpdate()
                    .set(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME, excluded(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME))
                    .set(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateCurrentCodes(Set<MarketDataCaptureProcessCode> currentCodes) {
        if (currentCodes == null) {
            throw new IllegalArgumentException("currentCodes must not be null");
        }
        if (currentCodes.isEmpty()) {
            throw new IllegalArgumentException("currentCodes must not be empty");
        }

        for (MarketDataCaptureProcessCode code : currentCodes) {
            if (code == null) {
                throw new IllegalArgumentException("currentCodes must not contain null");
            }
        }
    }

    private static void validatePassportsMap(Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passports) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }
    }

    private static List<Map.Entry<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport>> toValidatedEntries(
            Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passports
    ) {
        List<Map.Entry<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport>> entries = new ArrayList<>(passports.size());
        for (Map.Entry<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> entry : passports.entrySet()) {
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
