package com.alligator.market.backend.provider.readmodel.passport.query.port.adapter.jooq;

import com.alligator.market.domain.provider.passport.AccessMethod;
import com.alligator.market.domain.provider.passport.DeliveryMode;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.backend.provider.application.passport.catalog.port.out.ProviderPassportQueryPort;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.jooq.DSLContext;
import org.jooq.Record5;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.ProviderPassport.PROVIDER_PASSPORT;

/**
 * jOOQ-адаптер query-порта {@link ProviderPassportQueryPort}.
 *
 * <p>Читает materialized view таблицы {@code provider_passport} для отдачи данных в UI/каталог.</p>
 */
public final class ProviderPassportQueryPortJooqAdapter implements ProviderPassportQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public ProviderPassportQueryPortJooqAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Map<ProviderCode, ProviderPassport> findAll() {
        Map<ProviderCode, ProviderPassport> result = new LinkedHashMap<>();

        for (Record5<String, String, String, String, Boolean> row : dsl
                .select(
                        PROVIDER_PASSPORT.PROVIDER_CODE,
                        PROVIDER_PASSPORT.DISPLAY_NAME,
                        PROVIDER_PASSPORT.DELIVERY_MODE,
                        PROVIDER_PASSPORT.ACCESS_METHOD,
                        PROVIDER_PASSPORT.BULK_SUBSCRIPTION
                )
                .from(PROVIDER_PASSPORT)
                .orderBy(PROVIDER_PASSPORT.PROVIDER_CODE.asc())
                .fetch()) {
            ProviderCode code = ProviderCode.of(requireNonNull(row.value1(), "provider_code"));
            ProviderPassport passport = mapPassport(row);

            ProviderPassport prev = result.put(code, passport);
            if (prev != null) {
                // В норме невозможно из-за UNIQUE(provider_code), но если случится — лучше упасть явно.
                throw new IllegalStateException("Duplicate provider_code in provider_passport: " + code.value());
            }
        }

        return Collections.unmodifiableMap(result);
    }

    private static ProviderPassport mapPassport(Record5<String, String, String, String, Boolean> row) {
        String displayName = requireNonNull(row.value2(), "display_name");
        DeliveryMode deliveryMode = enumValue(DeliveryMode.class, requireNonNull(row.value3(), "delivery_mode"));
        AccessMethod accessMethod = enumValue(AccessMethod.class, requireNonNull(row.value4(), "access_method"));
        boolean bulk = requireNonNull(row.value5(), "bulk_subscription");

        return new ProviderPassport(displayName, deliveryMode, accessMethod, bulk);
    }

    private static <T> T requireNonNull(T value, String column) {
        if (value == null) {
            throw new IllegalStateException("Column '" + column + "' must not be null");
        }
        return value;
    }

    private static <E extends Enum<E>> E enumValue(Class<E> enumClass, String dbValue) {
        try {
            return Enum.valueOf(enumClass, dbValue);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Unknown " + enumClass.getSimpleName() + " value: " + dbValue, ex);
        }
    }
}
