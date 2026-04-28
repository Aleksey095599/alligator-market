package com.alligator.market.backend.provider.readmodel.passport.projection.port.adapter.jooq;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.readmodel.passport.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.TableField;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;
import static org.jooq.impl.DSL.excluded;

/**
 * jOOQ-реализация write-порта {@link ProviderPassportProjectionWritePort}.
 */
public class ProviderPassportProjectionWritePortJooqAdapter implements ProviderPassportProjectionWritePort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public ProviderPassportProjectionWritePortJooqAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void deleteAllExcept(Set<ProviderCode> activeCodes) {
        validateActiveCodes(activeCodes);

        // Преобразуем VO в набор строковых кодов для SQL-предиката NOT IN.
        Set<String> activeValues = new LinkedHashSet<>(activeCodes.size());
        for (ProviderCode code : activeCodes) {
            activeValues.add(code.value());
        }

        dsl.deleteFrom(PROVIDER_PASSPORT)
                .where(PROVIDER_PASSPORT.PROVIDER_CODE.notIn(activeValues))
                .execute();
    }

    @Override
    public void upsertAll(Map<ProviderCode, ProviderPassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return; // Контракт: пустая карта = no-op.
        }

        List<Map.Entry<ProviderCode, ProviderPassport>> entries = toValidatedEntries(passports);
        List<Query> queries = new ArrayList<>(entries.size());

        for (Map.Entry<ProviderCode, ProviderPassport> entry : entries) {
            ProviderCode code = entry.getKey();
            ProviderPassport passport = entry.getValue();

            Condition businessFieldsChanged = PROVIDER_PASSPORT.DISPLAY_NAME.isDistinctFrom(excluded(PROVIDER_PASSPORT.DISPLAY_NAME))
                    .or(PROVIDER_PASSPORT.DELIVERY_MODE.isDistinctFrom(excluded(PROVIDER_PASSPORT.DELIVERY_MODE)))
                    .or(PROVIDER_PASSPORT.ACCESS_METHOD.isDistinctFrom(excluded(PROVIDER_PASSPORT.ACCESS_METHOD)))
                    .or(PROVIDER_PASSPORT.BULK_SUBSCRIPTION.isDistinctFrom(excluded(PROVIDER_PASSPORT.BULK_SUBSCRIPTION)));

            Query query = dsl.insertInto(PROVIDER_PASSPORT)
                    .set(PROVIDER_PASSPORT.PROVIDER_CODE, code.value())
                    .set(PROVIDER_PASSPORT.DISPLAY_NAME, passport.displayName())
                    .set(PROVIDER_PASSPORT.DELIVERY_MODE, passport.deliveryMode().name())
                    .set(PROVIDER_PASSPORT.ACCESS_METHOD, passport.accessMethod().name())
                    .set(PROVIDER_PASSPORT.BULK_SUBSCRIPTION, passport.bulkSubscription())
                    .set(PROVIDER_PASSPORT.VERSION, numericLiteral(PROVIDER_PASSPORT.VERSION, 0))
                    .onConflict(PROVIDER_PASSPORT.PROVIDER_CODE)
                    .doUpdate()
                    .set(PROVIDER_PASSPORT.DISPLAY_NAME, excluded(PROVIDER_PASSPORT.DISPLAY_NAME))
                    .set(PROVIDER_PASSPORT.DELIVERY_MODE, excluded(PROVIDER_PASSPORT.DELIVERY_MODE))
                    .set(PROVIDER_PASSPORT.ACCESS_METHOD, excluded(PROVIDER_PASSPORT.ACCESS_METHOD))
                    .set(PROVIDER_PASSPORT.BULK_SUBSCRIPTION, excluded(PROVIDER_PASSPORT.BULK_SUBSCRIPTION))
                    .set(
                            PROVIDER_PASSPORT.VERSION,
                            PROVIDER_PASSPORT.VERSION.plus(numericLiteral(PROVIDER_PASSPORT.VERSION, 1))
                    )
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    /* Контракт deleteAllExcept: set не null/не пустой/без null-элементов. */
    private static void validateActiveCodes(Set<ProviderCode> activeCodes) {
        if (activeCodes == null) {
            throw new IllegalArgumentException("activeCodes must not be null");
        }
        if (activeCodes.isEmpty()) {
            throw new IllegalArgumentException("activeCodes must not be empty");
        }

        for (ProviderCode code : activeCodes) {
            if (code == null) {
                throw new IllegalArgumentException("activeCodes must not contain null");
            }
        }
    }

    /* Контракт upsertAll: map не null. */
    private static void validatePassportsMap(Map<ProviderCode, ProviderPassport> passports) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }
    }

    /* Контракт upsertAll: map без null-ключей и null-значений. */
    private static List<Map.Entry<ProviderCode, ProviderPassport>> toValidatedEntries(
            Map<ProviderCode, ProviderPassport> passports
    ) {
        List<Map.Entry<ProviderCode, ProviderPassport>> entries = new ArrayList<>(passports.size());
        for (Map.Entry<ProviderCode, ProviderPassport> entry : passports.entrySet()) {
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

    /* Типобезопасный numeric literal под тип VERSION-поля таблицы. */
    private static <N extends Number> Field<N> numericLiteral(TableField<?, N> field, Number value) {
        return DSL.val(value).cast(field.getDataType());
    }
}
