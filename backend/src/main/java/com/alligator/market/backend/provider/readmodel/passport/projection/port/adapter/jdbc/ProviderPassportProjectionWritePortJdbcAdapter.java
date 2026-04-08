package com.alligator.market.backend.provider.readmodel.passport.projection.port.adapter.jdbc;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.passport.projection.port.ProviderPassportProjectionWritePort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * JDBC-реализация write-порта {@link ProviderPassportProjectionWritePort} для PostgreSQL.
 */
public class ProviderPassportProjectionWritePortJdbcAdapter implements ProviderPassportProjectionWritePort {

    // Удаляем всё, кроме activeCodes: deleteAllExcept(Set).
    private static final String SQL_DELETE_ALL_EXCEPT = """
            DELETE FROM provider_passport
            WHERE provider_code <> ALL (?)
            """;

    // upsertAll(Map): вставка/обновление, version растёт только при изменении бизнес-полей.
    private static final String SQL_UPSERT = """
            INSERT INTO provider_passport(
              provider_code,
              display_name,
              delivery_mode,
              access_method,
              bulk_subscription,
              version
            ) VALUES (?, ?, ?, ?, ?, 0)
            ON CONFLICT (provider_code) DO UPDATE SET
              display_name = EXCLUDED.display_name,
              delivery_mode = EXCLUDED.delivery_mode,
              access_method = EXCLUDED.access_method,
              bulk_subscription = EXCLUDED.bulk_subscription,
              version = provider_passport.version + 1
            WHERE
              provider_passport.display_name IS DISTINCT FROM EXCLUDED.display_name
              OR provider_passport.delivery_mode IS DISTINCT FROM EXCLUDED.delivery_mode
              OR provider_passport.access_method IS DISTINCT FROM EXCLUDED.access_method
              OR provider_passport.bulk_subscription IS DISTINCT FROM EXCLUDED.bulk_subscription
            """;

    private final JdbcTemplate jdbc;

    public ProviderPassportProjectionWritePortJdbcAdapter(JdbcTemplate jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc must not be null");
    }

    @Override
    public void deleteAllExcept(Set<ProviderCode> activeCodes) {
        validateActiveCodes(activeCodes);

        // Маппим activeCodes в SQL-массив для одного DELETE-запроса.
        final String[] values = activeCodes.stream()
                .map(ProviderCode::value)
                .toArray(String[]::new);

        jdbc.execute((ConnectionCallback<Integer>) con -> {
            try (PreparedStatement ps = con.prepareStatement(SQL_DELETE_ALL_EXCEPT)) {
                Array array = con.createArrayOf("varchar", values);
                try {
                    ps.setArray(1, array);
                    return ps.executeUpdate();
                } finally {
                    array.free();
                }
            }
        });
    }

    @Override
    public void upsertAll(Map<ProviderCode, ProviderPassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return; // Контракт: пустая карта = no-op.
        }

        // Контракт: null-ключи и null-значения запрещены.
        List<Map.Entry<ProviderCode, ProviderPassport>> entries = toValidatedEntries(passports);

        // Batch для минимального числа round-trip.
        jdbc.batchUpdate(SQL_UPSERT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@org.springframework.lang.NonNull PreparedStatement ps, int i) throws SQLException {
                bindUpsert(ps, entries.get(i));
            }

            @Override
            public int getBatchSize() {
                return entries.size();
            }
        });
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

    /** Привязка параметров UPSERT для одной записи. */
    private static void bindUpsert(
            PreparedStatement ps,
            Map.Entry<ProviderCode, ProviderPassport> entry
    ) throws SQLException {
        Objects.requireNonNull(ps, "ps must not be null");
        Objects.requireNonNull(entry, "entry must not be null");

        ProviderCode code = entry.getKey();
        ProviderPassport passport = entry.getValue();

        // 1) Идентификатор записи.
        ps.setString(1, code.value());

        // 2) Бизнес-поля паспорта.
        ps.setString(2, passport.displayName());
        ps.setString(3, passport.deliveryMode().name());
        ps.setString(4, passport.accessMethod().name());
        ps.setBoolean(5, passport.bulkSubscription());
    }
}
