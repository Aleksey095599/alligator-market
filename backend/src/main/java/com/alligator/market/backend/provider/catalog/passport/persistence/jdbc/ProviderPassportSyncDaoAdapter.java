package com.alligator.market.backend.provider.catalog.passport.persistence.jdbc;

import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.db.dao.ProviderPassportSyncDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * JDBC-адаптер для пакетной синхронизации паспортов провайдеров.
 * Использует PostgreSQL Native UPSERT (ON CONFLICT) для обеспечения производительности.
 */
@Repository
public class ProviderPassportSyncDaoAdapter implements ProviderPassportSyncDao {

    /* Spring JdbcTemplate. */
    private final JdbcTemplate jdbc;

    /* Конструктор. */
    public ProviderPassportSyncDaoAdapter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Пакетное удаление (DELETE) паспортов по их кодам.
     */
    @Override
    public void deleteByCodes(Collection<ProviderCode> codes) {
        if (codes == null || codes.isEmpty()) return;

        // SQL-команда
        final String sql = "DELETE FROM provider_passport WHERE provider_code = ?";

        // Преобразуем коллекцию в массив (предполагаем, что коды уже нормализованы)
        String[] arr = codes.stream()
                .filter(Objects::nonNull)
                .map(ProviderCode::value)
                .toArray(String[]::new);

        // Пакетно выполняем DELETE: привязываем provider_code по индексу и задаём размер батча.
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@org.springframework.lang.NonNull PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, arr[i]);
            }

            @Override
            public int getBatchSize() {
                return arr.length;
            }
        });
    }

    /**
     * Пакетная вставка или обновление (UPSERT) паспортов.
     */
    @Override
    public void upsertAll(Map<ProviderCode, ProviderPassport> providerPassports) {
        if (providerPassports == null || providerPassports.isEmpty()) return;

        // Берём аудит-атрибуты из контекст-холдера
        final String actor = AuditContextHolder.actorOrFallback();
        final String via = AuditContextHolder.viaOrFallback();

        // SQL-команда
        final String sql = """
                INSERT INTO provider_passport(
                  provider_code,
                  display_name,
                  delivery_mode,
                  access_method,
                  bulk_subscription,
                  version,
                  created_timestamp,
                  created_by,
                  created_via,
                  updated_timestamp,
                  updated_by,
                  updated_via
                ) VALUES (?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                ON CONFLICT (provider_code) DO UPDATE SET
                  display_name = EXCLUDED.display_name,
                  delivery_mode = EXCLUDED.delivery_mode,
                  access_method = EXCLUDED.access_method,
                  bulk_subscription = EXCLUDED.bulk_subscription,
                  version = provider_passport.version + 1,
                  updated_timestamp = CURRENT_TIMESTAMP,
                  updated_by = EXCLUDED.updated_by,
                  updated_via = EXCLUDED.updated_via
                """;

        // Преобразуем набор в список – нужен индекс для BatchPreparedStatementSetter и фиксированный размер батча.
        List<Map.Entry<ProviderCode, ProviderPassport>> entries = providerPassports.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .toList();

        // Пакетный UPSERT: берём запись arr[i], привязываем параметры через bindUpsert(...), задаём размер батча.
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@org.springframework.lang.NonNull PreparedStatement ps, int i) throws SQLException {
                bindUpsert(ps, entries.get(i), actor, via);
            }

            @Override
            public int getBatchSize() {
                return entries.size();
            }
        });
    }

    /**
     * Привязка параметров для одной строки UPSERT (заполнение параметров "?" в SQL-команде конкретными значениями).
     */
    private void bindUpsert(PreparedStatement ps,
                            Map.Entry<ProviderCode, ProviderPassport> entry,
                            String actor,
                            String via) throws SQLException {
        Objects.requireNonNull(entry, "entry must not be null");
        Objects.requireNonNull(actor, "actor must not be null");
        Objects.requireNonNull(via, "via must not be null");
        // Примечание: иные проверки не обязательны – корректность данных гарантируется моделью ProviderPassport

        // 1) provider_code (натуральный ключ)
        ProviderCode code = entry.getKey();
        ProviderPassport passport = entry.getValue();
        ps.setString(1, code.value());

        // 2) passport.*
        ps.setString(2, passport.displayName());
        ps.setString(3, passport.deliveryMode().name());
        ps.setString(4, passport.accessMethod().name());
        ps.setBoolean(5, passport.bulkSubscription());

        // 3) audit-атрибуты (actor/via). Используем одинаковые значения для created_* и updated_*.
        ps.setString(6, actor);
        ps.setString(7, via);
        ps.setString(8, actor);
        ps.setString(9, via);
    }
}
