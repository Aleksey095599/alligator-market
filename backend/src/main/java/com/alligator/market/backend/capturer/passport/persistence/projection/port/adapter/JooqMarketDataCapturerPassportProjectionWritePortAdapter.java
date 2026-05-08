package com.alligator.market.backend.capturer.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.capturer.passport.application.projection.port.MarketDataCapturerPassportProjectionWritePort;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
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
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static org.jooq.impl.DSL.excluded;

public class JooqMarketDataCapturerPassportProjectionWritePortAdapter
        implements MarketDataCapturerPassportProjectionWritePort {
    private final DSLContext dsl;

    public JooqMarketDataCapturerPassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireAllExcept(Set<MarketDataCapturerCode> currentCodes) {
        validateCurrentCodes(currentCodes);

        Set<String> currentValues = new LinkedHashSet<>(currentCodes.size());
        for (MarketDataCapturerCode code : currentCodes) {
            currentValues.add(code.value());
        }

        dsl.update(MARKET_DATA_CAPTURER_PASSPORT)
                .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.notIn(currentValues))
                .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void upsertAll(Map<MarketDataCapturerCode, MarketDataCapturerPassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return;
        }

        List<Map.Entry<MarketDataCapturerCode, MarketDataCapturerPassport>> entries = toValidatedEntries(passports);
        List<Query> queries = new ArrayList<>(entries.size());

        for (Map.Entry<MarketDataCapturerCode, MarketDataCapturerPassport> entry : entries) {
            MarketDataCapturerCode code = entry.getKey();
            MarketDataCapturerPassport passport = entry.getValue();

            Condition businessFieldsChanged = MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                    .or(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(ACTIVE.name()));

            Query query = dsl.insertInto(MARKET_DATA_CAPTURER_PASSPORT)
                    .set(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE, code.value())
                    .set(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .onConflict(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE)
                    .doUpdate()
                    .set(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME, excluded(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                    .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateCurrentCodes(Set<MarketDataCapturerCode> currentCodes) {
        if (currentCodes == null) {
            throw new IllegalArgumentException("currentCodes must not be null");
        }
        if (currentCodes.isEmpty()) {
            throw new IllegalArgumentException("currentCodes must not be empty");
        }

        for (MarketDataCapturerCode code : currentCodes) {
            if (code == null) {
                throw new IllegalArgumentException("currentCodes must not contain null");
            }
        }
    }

    private static void validatePassportsMap(Map<MarketDataCapturerCode, MarketDataCapturerPassport> passports) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }
    }

    private static List<Map.Entry<MarketDataCapturerCode, MarketDataCapturerPassport>> toValidatedEntries(
            Map<MarketDataCapturerCode, MarketDataCapturerPassport> passports
    ) {
        List<Map.Entry<MarketDataCapturerCode, MarketDataCapturerPassport>> entries = new ArrayList<>(passports.size());
        for (Map.Entry<MarketDataCapturerCode, MarketDataCapturerPassport> entry : passports.entrySet()) {
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
