package com.alligator.market.backend.source.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.source.passport.application.projection.port.MarketDataSourcePassportProjectionWritePort;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
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
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataSourcePassport.MARKET_DATA_SOURCE_PASSPORT;
import static org.jooq.impl.DSL.excluded;

/**
 * jOOQ implementation of {@link MarketDataSourcePassportProjectionWritePort}.
 */
public class JooqMarketDataSourcePassportProjectionWritePortAdapter implements MarketDataSourcePassportProjectionWritePort {

    private final DSLContext dsl;

    public JooqMarketDataSourcePassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireAllExcept(Set<MarketDataSourceCode> currentCodes) {
        validateCurrentCodes(currentCodes);

        Set<String> currentValues = new LinkedHashSet<>(currentCodes.size());
        for (MarketDataSourceCode code : currentCodes) {
            currentValues.add(code.value());
        }

        dsl.update(MARKET_DATA_SOURCE_PASSPORT)
                .set(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE.notIn(currentValues))
                .and(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void upsertAll(Map<MarketDataSourceCode, MarketDataSourcePassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return;
        }

        List<Map.Entry<MarketDataSourceCode, MarketDataSourcePassport>> entries = toValidatedEntries(passports);
        List<Query> queries = new ArrayList<>(entries.size());

        for (Map.Entry<MarketDataSourceCode, MarketDataSourcePassport> entry : entries) {
            MarketDataSourceCode code = entry.getKey();
            MarketDataSourcePassport passport = entry.getValue();

            Condition businessFieldsChanged = MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME))
                    .or(MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE
                            .isDistinctFrom(excluded(MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE)))
                    .or(MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD
                            .isDistinctFrom(excluded(MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD)))
                    .or(MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION
                            .isDistinctFrom(excluded(MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION)))
                    .or(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(ACTIVE.name()));

            Query query = dsl.insertInto(MARKET_DATA_SOURCE_PASSPORT)
                    .set(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE, code.value())
                    .set(MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE, passport.deliveryMode().name())
                    .set(MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD, passport.accessMethod().name())
                    .set(MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION, passport.bulkSubscription())
                    .set(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .onConflict(MARKET_DATA_SOURCE_PASSPORT.SOURCE_CODE)
                    .doUpdate()
                    .set(MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME,
                            excluded(MARKET_DATA_SOURCE_PASSPORT.DISPLAY_NAME))
                    .set(MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE,
                            excluded(MARKET_DATA_SOURCE_PASSPORT.DELIVERY_MODE))
                    .set(MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD,
                            excluded(MARKET_DATA_SOURCE_PASSPORT.ACCESS_METHOD))
                    .set(MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION,
                            excluded(MARKET_DATA_SOURCE_PASSPORT.BULK_SUBSCRIPTION))
                    .set(MARKET_DATA_SOURCE_PASSPORT.LIFECYCLE_STATUS, ACTIVE.name())
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateCurrentCodes(Set<MarketDataSourceCode> currentCodes) {
        if (currentCodes == null) {
            throw new IllegalArgumentException("currentCodes must not be null");
        }
        if (currentCodes.isEmpty()) {
            throw new IllegalArgumentException("currentCodes must not be empty");
        }

        for (MarketDataSourceCode code : currentCodes) {
            if (code == null) {
                throw new IllegalArgumentException("currentCodes must not contain null");
            }
        }
    }

    private static void validatePassportsMap(Map<MarketDataSourceCode, MarketDataSourcePassport> passports) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }
    }

    private static List<Map.Entry<MarketDataSourceCode, MarketDataSourcePassport>> toValidatedEntries(
            Map<MarketDataSourceCode, MarketDataSourcePassport> passports
    ) {
        List<Map.Entry<MarketDataSourceCode, MarketDataSourcePassport>> entries = new ArrayList<>(passports.size());
        for (Map.Entry<MarketDataSourceCode, MarketDataSourcePassport> entry : passports.entrySet()) {
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
