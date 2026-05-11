package com.alligator.market.backend.capturer.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.capturer.passport.application.projection.port.MarketDataCapturerPassportProjectionWritePort;
import com.alligator.market.backend.capturer.passport.persistence.projection.mapper.StoredMarketDataCapturerPassportMapper;
import com.alligator.market.backend.capturer.passport.persistence.projection.model.StoredMarketDataCapturerPassport;
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

import static com.alligator.market.backend.capturer.passport.persistence.projection.model.StoredMarketDataCapturerProjectionLifecycleStatus.RETIRED;
import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static org.jooq.impl.DSL.excluded;

public class JooqMarketDataCapturerPassportProjectionWritePortAdapter
        implements MarketDataCapturerPassportProjectionWritePort {
    private final DSLContext dsl;
    private final StoredMarketDataCapturerPassportMapper storedPassportMapper;

    public JooqMarketDataCapturerPassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
        this.storedPassportMapper = new StoredMarketDataCapturerPassportMapper();
    }

    @Override
    public void retireAllExcept(Set<MarketDataCapturerCode> passportCodes) {
        validateCurrentCodes(passportCodes);

        Set<String> currentValues = new LinkedHashSet<>(passportCodes.size());
        for (MarketDataCapturerCode code : passportCodes) {
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
        List<StoredMarketDataCapturerPassport> storedPassports = storedPassportMapper.toActiveStored(passports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (StoredMarketDataCapturerPassport storedPassport : storedPassports) {
            MarketDataCapturerCode code = storedPassport.capturerCode();
            MarketDataCapturerPassport passport = storedPassport.passport();
            String lifecycleStatus = storedPassport.lifecycleStatus().name();

            Condition businessFieldsChanged = MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                    .or(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(lifecycleStatus));

            Query query = dsl.insertInto(MARKET_DATA_CAPTURER_PASSPORT)
                    .set(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE, code.value())
                    .set(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, lifecycleStatus)
                    .onConflict(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE)
                    .doUpdate()
                    .set(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME, excluded(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                    .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, lifecycleStatus)
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
}
