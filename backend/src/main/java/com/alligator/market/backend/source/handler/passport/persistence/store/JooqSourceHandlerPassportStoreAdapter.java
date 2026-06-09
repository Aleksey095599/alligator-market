package com.alligator.market.backend.source.handler.passport.persistence.store;

import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportKey;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportRecord;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportStore;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportRecord.RegistryStatus.RETIRED;
import static org.jooq.impl.DSL.excluded;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

public class JooqSourceHandlerPassportStoreAdapter implements SourceHandlerPassportStore {
    private static final Table<?> SOURCE_HANDLER_PASSPORT =
            table(name("source_handler_passport"));
    private static final Field<String> SOURCE_HANDLER_PASSPORT_SOURCE_CODE =
            field(name("source_code"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_HANDLER_CODE =
            field(name("handler_code"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_DELIVERY_MODE =
            field(name("delivery_mode"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_ACCESS_METHOD =
            field(name("access_method"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS =
            field(name("registry_status"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_STORED_DELIVERY_MODE =
            field(name("source_handler_passport", "delivery_mode"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_STORED_ACCESS_METHOD =
            field(name("source_handler_passport", "access_method"), String.class);
    private static final Field<String> SOURCE_HANDLER_PASSPORT_STORED_REGISTRY_STATUS =
            field(name("source_handler_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqSourceHandlerPassportStoreAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void retireAllExcept(Set<SourceHandlerPassportKey> registeredSourceHandlerPassportKeys) {
        validateRegisteredKeys(registeredSourceHandlerPassportKeys);

        Condition registeredPassport = falseCondition();
        for (SourceHandlerPassportKey key : registeredSourceHandlerPassportKeys) {
            registeredPassport = registeredPassport.or(
                    SOURCE_HANDLER_PASSPORT_SOURCE_CODE.eq(key.sourceCode().value())
                            .and(SOURCE_HANDLER_PASSPORT_HANDLER_CODE.eq(key.handlerCode().value()))
            );
        }

        dsl.update(SOURCE_HANDLER_PASSPORT)
                .set(SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS, RETIRED.name())
                .where(registeredPassport.not())
                .and(SOURCE_HANDLER_PASSPORT_STORED_REGISTRY_STATUS.isDistinctFrom(RETIRED.name()))
                .execute();
    }

    @Override
    public void save(Collection<SourceHandlerPassportRecord> passports) {
        Objects.requireNonNull(passports, "passports must not be null");

        List<SourceHandlerPassportRecord> storedPassports = List.copyOf(passports);
        if (storedPassports.isEmpty()) {
            return;
        }

        List<Query> queries = new ArrayList<>(storedPassports.size());

        for (SourceHandlerPassportRecord storedPassport : storedPassports) {
            SourceHandlerPassport passport = storedPassport.passport();
            String registryStatus = storedPassport.registryStatus().name();

            Condition businessFieldsChanged = SOURCE_HANDLER_PASSPORT_STORED_DELIVERY_MODE
                    .isDistinctFrom(excluded(SOURCE_HANDLER_PASSPORT_DELIVERY_MODE))
                    .or(SOURCE_HANDLER_PASSPORT_STORED_ACCESS_METHOD
                            .isDistinctFrom(excluded(SOURCE_HANDLER_PASSPORT_ACCESS_METHOD)))
                    .or(SOURCE_HANDLER_PASSPORT_STORED_REGISTRY_STATUS.isDistinctFrom(registryStatus));

            Query query = dsl.insertInto(SOURCE_HANDLER_PASSPORT)
                    .set(SOURCE_HANDLER_PASSPORT_SOURCE_CODE, storedPassport.sourceCode().value())
                    .set(SOURCE_HANDLER_PASSPORT_HANDLER_CODE, storedPassport.handlerCode().value())
                    .set(SOURCE_HANDLER_PASSPORT_DELIVERY_MODE, passport.deliveryMode().code())
                    .set(SOURCE_HANDLER_PASSPORT_ACCESS_METHOD, passport.accessMethod().code())
                    .set(SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS, registryStatus)
                    .onConflict(
                            SOURCE_HANDLER_PASSPORT_SOURCE_CODE,
                            SOURCE_HANDLER_PASSPORT_HANDLER_CODE
                    )
                    .doUpdate()
                    .set(SOURCE_HANDLER_PASSPORT_DELIVERY_MODE,
                            excluded(SOURCE_HANDLER_PASSPORT_DELIVERY_MODE))
                    .set(SOURCE_HANDLER_PASSPORT_ACCESS_METHOD,
                            excluded(SOURCE_HANDLER_PASSPORT_ACCESS_METHOD))
                    .set(SOURCE_HANDLER_PASSPORT_REGISTRY_STATUS, registryStatus)
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    private static void validateRegisteredKeys(Set<SourceHandlerPassportKey> registeredSourceHandlerPassportKeys) {
        if (registeredSourceHandlerPassportKeys == null) {
            throw new IllegalArgumentException("registeredSourceHandlerPassportKeys must not be null");
        }
        if (registeredSourceHandlerPassportKeys.isEmpty()) {
            throw new IllegalArgumentException("registeredSourceHandlerPassportKeys must not be empty");
        }

        Set<SourceHandlerPassportKey> uniqueKeys =
                new LinkedHashSet<>(registeredSourceHandlerPassportKeys.size());
        for (SourceHandlerPassportKey key : registeredSourceHandlerPassportKeys) {
            if (key == null) {
                throw new IllegalArgumentException("registeredSourceHandlerPassportKeys must not contain null");
            }
            if (!uniqueKeys.add(key)) {
                throw new IllegalArgumentException(
                        "registeredSourceHandlerPassportKeys must not contain duplicate keys"
                );
            }
        }
    }
}
