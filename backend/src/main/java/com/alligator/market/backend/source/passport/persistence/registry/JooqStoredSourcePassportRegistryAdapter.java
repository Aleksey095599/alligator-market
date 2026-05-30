package com.alligator.market.backend.source.passport.persistence.registry;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassport.Status.RETIRED;
import static org.jooq.impl.DSL.excluded;

public class JooqStoredSourcePassportRegistryAdapter implements StoredSourcePassportRegistry {
    private final DSLContext dsl;

    public JooqStoredSourcePassportRegistryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireAllExcept(Set<SourceCode> registeredSourceCodes) {
        validateRegisteredCodes(registeredSourceCodes);

        Set<String> currentValues = new LinkedHashSet<>(registeredSourceCodes.size());
        for (SourceCode code : registeredSourceCodes) {
            currentValues.add(code.value());
        }

        dsl.update(SOURCE_PASSPORT)
                .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, RETIRED.name())
                .where(SOURCE_PASSPORT.SOURCE_CODE.notIn(currentValues))
                .and(SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void save(Collection<StoredSourcePassport> passports) {
        Objects.requireNonNull(passports, "passports must not be null");

        List<StoredSourcePassport> storedPassports = List.copyOf(passports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (StoredSourcePassport storedPassport : storedPassports) {
            SourceCode code = storedPassport.sourceCode();
            SourcePassport passport = storedPassport.passport();
            String registryStatus = storedPassport.status().name();

            Condition businessFieldsChanged = SOURCE_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(SOURCE_PASSPORT.DISPLAY_NAME))
                    .or(SOURCE_PASSPORT.LIFECYCLE_STATUS.isDistinctFrom(registryStatus));

            Query query = dsl.insertInto(SOURCE_PASSPORT)
                    .set(SOURCE_PASSPORT.SOURCE_CODE, code.value())
                    .set(SOURCE_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .onConflict(SOURCE_PASSPORT.SOURCE_CODE)
                    .doUpdate()
                    .set(SOURCE_PASSPORT.DISPLAY_NAME,
                            excluded(SOURCE_PASSPORT.DISPLAY_NAME))
                    .set(SOURCE_PASSPORT.LIFECYCLE_STATUS, registryStatus)
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateRegisteredCodes(Set<SourceCode> registeredSourceCodes) {
        if (registeredSourceCodes == null) {
            throw new IllegalArgumentException("registeredSourceCodes must not be null");
        }
        if (registeredSourceCodes.isEmpty()) {
            throw new IllegalArgumentException("registeredSourceCodes must not be empty");
        }

        for (SourceCode code : registeredSourceCodes) {
            if (code == null) {
                throw new IllegalArgumentException("registeredSourceCodes must not contain null");
            }
        }
    }
}
