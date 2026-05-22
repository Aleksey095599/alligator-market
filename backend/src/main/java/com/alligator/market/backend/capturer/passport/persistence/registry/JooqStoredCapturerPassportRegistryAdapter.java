package com.alligator.market.backend.capturer.passport.persistence.registry;

import com.alligator.market.backend.capturer.passport.persistence.mapper.StoredMarketDataCapturerPassportMapper;
import com.alligator.market.backend.capturer.passport.persistence.model.StoredMarketDataCapturerPassport;
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

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
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
    public void retireAllExcept(Set<CapturerCode> registeredCapturerCodes) {
        validateRegisteredCodes(registeredCapturerCodes);

        Set<String> currentValues = new LinkedHashSet<>(registeredCapturerCodes.size());
        for (CapturerCode code : registeredCapturerCodes) {
            currentValues.add(code.value());
        }

        dsl.update(CAPTURER_PASSPORT)
                .set(CAPTURER_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(CAPTURER_PASSPORT.CAPTURER_CODE.notIn(currentValues))
                .and(CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void saveRegistered(Map<CapturerCode, CapturerPassport> registeredPassports) {
        List<StoredMarketDataCapturerPassport> storedPassports =
                storedPassportMapper.toRegisteredStored(registeredPassports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (StoredMarketDataCapturerPassport storedPassport : storedPassports) {
            CapturerCode code = storedPassport.capturerCode();
            CapturerPassport passport = storedPassport.passport();
            String registryStatus = storedPassport.registryStatus().name();

            Condition businessFieldsChanged = CAPTURER_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(CAPTURER_PASSPORT.DISPLAY_NAME))
                    .or(CAPTURER_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(registryStatus));

            Query query = dsl.insertInto(CAPTURER_PASSPORT)
                    .set(CAPTURER_PASSPORT.CAPTURER_CODE, code.value())
                    .set(CAPTURER_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(CAPTURER_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .onConflict(CAPTURER_PASSPORT.CAPTURER_CODE)
                    .doUpdate()
                    .set(CAPTURER_PASSPORT.DISPLAY_NAME, excluded(CAPTURER_PASSPORT.DISPLAY_NAME))
                    .set(CAPTURER_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateRegisteredCodes(Set<CapturerCode> registeredCapturerCodes) {
        if (registeredCapturerCodes == null) {
            throw new IllegalArgumentException("registeredCapturerCodes must not be null");
        }
        if (registeredCapturerCodes.isEmpty()) {
            throw new IllegalArgumentException("registeredCapturerCodes must not be empty");
        }

        for (CapturerCode code : registeredCapturerCodes) {
            if (code == null) {
                throw new IllegalArgumentException("registeredCapturerCodes must not contain null");
            }
        }
    }
}
