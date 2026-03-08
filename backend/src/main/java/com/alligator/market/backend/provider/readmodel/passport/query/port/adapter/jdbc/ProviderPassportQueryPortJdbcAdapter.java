package com.alligator.market.backend.provider.readmodel.passport.query.port.adapter.jdbc;

import com.alligator.market.domain.marketdata.provider.model.passport.AccessMethod;
import com.alligator.market.domain.marketdata.provider.model.passport.DeliveryMode;
import com.alligator.market.domain.marketdata.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;
import com.alligator.market.domain.marketdata.provider.readmodel.passport.query.port.ProviderPassportQueryPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JDBC-адаптер query-порта {@link ProviderPassportQueryPort}.
 *
 * <p>Читает materialized view таблицы {@code provider_passport} для отдачи данных в UI/каталог.</p>
 */
public final class ProviderPassportQueryPortJdbcAdapter implements ProviderPassportQueryPort {

    private static final String SQL_FIND_ALL = """
            SELECT
              provider_code,
              display_name,
              delivery_mode,
              access_method,
              bulk_subscription
            FROM provider_passport
            ORDER BY provider_code
            """;

    private final JdbcTemplate jdbc;

    public ProviderPassportQueryPortJdbcAdapter(JdbcTemplate jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc must not be null");
    }

    @Override
    public Map<ProviderCode, ProviderPassport> findAll() {
        Map<ProviderCode, ProviderPassport> result = new LinkedHashMap<>();

        jdbc.query(SQL_FIND_ALL, rs -> {
            ProviderCode code = ProviderCode.of(requireNonNull(rs, "provider_code"));
            ProviderPassport passport = mapPassport(rs);

            ProviderPassport prev = result.put(code, passport);
            if (prev != null) {
                // В норме невозможно из-за UNIQUE(provider_code), но если случится — лучше упасть явно.
                throw new IllegalStateException("Duplicate provider_code in provider_passport: " + code.value());
            }
        });

        return Collections.unmodifiableMap(result);
    }

    private static ProviderPassport mapPassport(ResultSet rs) throws SQLException {
        String displayName = requireNonNull(rs, "display_name");
        DeliveryMode deliveryMode = enumValue(DeliveryMode.class, requireNonNull(rs, "delivery_mode"));
        AccessMethod accessMethod = enumValue(AccessMethod.class, requireNonNull(rs, "access_method"));
        boolean bulk = rs.getBoolean("bulk_subscription");

        return new ProviderPassport(displayName, deliveryMode, accessMethod, bulk);
    }

    private static String requireNonNull(ResultSet rs, String column) throws SQLException {
        String value = rs.getString(column);
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
