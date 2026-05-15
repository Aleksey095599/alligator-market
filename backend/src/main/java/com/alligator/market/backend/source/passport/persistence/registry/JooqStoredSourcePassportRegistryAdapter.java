package com.alligator.market.backend.source.passport.persistence.registry;

import com.alligator.market.backend.source.passport.persistence.mapper.StoredSourcePassportMapper;
import com.alligator.market.backend.source.passport.persistence.model.StoredSourcePassport;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
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

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistryStatus.RETIRED;
import static org.jooq.impl.DSL.excluded;

public class JooqStoredSourcePassportRegistryAdapter implements StoredSourcePassportRegistry {
    private final DSLContext dsl;
    private final StoredSourcePassportMapper storedPassportMapper;

    public JooqStoredSourcePassportRegistryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
        this.storedPassportMapper = new StoredSourcePassportMapper();
    }

    @Override
    public void retireAllExcept(Set<SourceCode> activeSourceCodes) {
        validateActiveCodes(activeSourceCodes);

        Set<String> currentValues = new LinkedHashSet<>(activeSourceCodes.size());
        for (SourceCode code : activeSourceCodes) {
            currentValues.add(code.value());
        }

        dsl.update(SOURCE_PASSPORT)
                .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(SOURCE_PASSPORT.SOURCE_CODE.notIn(currentValues))
                .and(SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void saveActive(Map<SourceCode, SourcePassport> activePassports) {
        List<StoredSourcePassport> storedPassports = storedPassportMapper.toActiveStored(activePassports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (StoredSourcePassport storedPassport : storedPassports) {
            SourceCode code = storedPassport.sourceCode();
            SourcePassport passport = storedPassport.passport();
            String registryStatus = storedPassport.registryStatus().name();

            Condition businessFieldsChanged = SOURCE_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(SOURCE_PASSPORT.DISPLAY_NAME))
                    .or(SOURCE_PASSPORT.DELIVERY_MODE
                            .isDistinctFrom(excluded(SOURCE_PASSPORT.DELIVERY_MODE)))
                    .or(SOURCE_PASSPORT.ACCESS_METHOD
                            .isDistinctFrom(excluded(SOURCE_PASSPORT.ACCESS_METHOD)))
                    .or(SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(registryStatus));

            Query query = dsl.insertInto(SOURCE_PASSPORT)
                    .set(SOURCE_PASSPORT.SOURCE_CODE, code.value())
                    .set(SOURCE_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(SOURCE_PASSPORT.DELIVERY_MODE, passport.deliveryMode().code())
                    .set(SOURCE_PASSPORT.ACCESS_METHOD, passport.accessMethod().code())
                    .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .onConflict(SOURCE_PASSPORT.SOURCE_CODE)
                    .doUpdate()
                    .set(SOURCE_PASSPORT.DISPLAY_NAME,
                            excluded(SOURCE_PASSPORT.DISPLAY_NAME))
                    .set(SOURCE_PASSPORT.DELIVERY_MODE,
                            excluded(SOURCE_PASSPORT.DELIVERY_MODE))
                    .set(SOURCE_PASSPORT.ACCESS_METHOD,
                            excluded(SOURCE_PASSPORT.ACCESS_METHOD))
                    .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateActiveCodes(Set<SourceCode> activeSourceCodes) {
        if (activeSourceCodes == null) {
            throw new IllegalArgumentException("activeSourceCodes must not be null");
        }
        if (activeSourceCodes.isEmpty()) {
            throw new IllegalArgumentException("activeSourceCodes must not be empty");
        }

        for (SourceCode code : activeSourceCodes) {
            if (code == null) {
                throw new IllegalArgumentException("activeSourceCodes must not contain null");
            }
        }
    }
}
