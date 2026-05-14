package com.alligator.market.backend.capturer.passport.persistence.registry;

import com.alligator.market.backend.capturer.passport.persistence.projection.mapper.StoredMarketDataCapturerPassportMapper;
import com.alligator.market.backend.capturer.passport.persistence.projection.model.StoredMarketDataCapturerPassport;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.backend.infra.jooq.generated.tables.MarketDataCapturerPassport.MARKET_DATA_CAPTURER_PASSPORT;
import static com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistryStatus.RETIRED;
import static org.jooq.impl.DSL.excluded;

public class JooqStoredCapturerPassportRegistryAdapter implements StoredCapturerPassportRegistry {
    private final DSLContext dsl;
    private final StoredMarketDataCapturerPassportMapper storedPassportMapper;

    public JooqStoredCapturerPassportRegistryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
        this.storedPassportMapper = new StoredMarketDataCapturerPassportMapper();
    }

    @Override
    public void retireAllExcept(Set<CapturerCode> activeCapturerCodes) {
        validateActiveCodes(activeCapturerCodes);

        Set<String> currentValues = new LinkedHashSet<>(activeCapturerCodes.size());
        for (CapturerCode code : activeCapturerCodes) {
            currentValues.add(code.value());
        }

        dsl.update(MARKET_DATA_CAPTURER_PASSPORT)
                .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE.notIn(currentValues))
                .and(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void saveActive(Map<CapturerCode, CapturerPassport> activePassports) {
        List<StoredMarketDataCapturerPassport> storedPassports = storedPassportMapper.toActiveStored(activePassports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (StoredMarketDataCapturerPassport storedPassport : storedPassports) {
            CapturerCode code = storedPassport.capturerCode();
            CapturerPassport passport = storedPassport.passport();
            String registryStatus = storedPassport.registryStatus().name();

            Condition businessFieldsChanged = MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                    .or(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(registryStatus));

            Query query = dsl.insertInto(MARKET_DATA_CAPTURER_PASSPORT)
                    .set(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE, code.value())
                    .set(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .onConflict(MARKET_DATA_CAPTURER_PASSPORT.CAPTURER_CODE)
                    .doUpdate()
                    .set(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME, excluded(MARKET_DATA_CAPTURER_PASSPORT.DISPLAY_NAME))
                    .set(MARKET_DATA_CAPTURER_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateActiveCodes(Set<CapturerCode> activeCapturerCodes) {
        if (activeCapturerCodes == null) {
            throw new IllegalArgumentException("activeCapturerCodes must not be null");
        }
        if (activeCapturerCodes.isEmpty()) {
            throw new IllegalArgumentException("activeCapturerCodes must not be empty");
        }

        for (CapturerCode code : activeCapturerCodes) {
            if (code == null) {
                throw new IllegalArgumentException("activeCapturerCodes must not contain null");
            }
        }
    }
}
