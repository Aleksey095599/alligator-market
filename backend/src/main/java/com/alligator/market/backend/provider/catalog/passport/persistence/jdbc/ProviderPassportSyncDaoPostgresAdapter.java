package com.alligator.market.backend.provider.catalog.passport.persistence.jdbc;

import com.alligator.market.backend.common.persistence.jpa.converter.DurationToSecondsConverter;
import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.reconciliation.db.dao.ProviderPassportSyncDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * Адаптер доменного DAO {@link ProviderPassportSyncDao} в контексте БД PostgreSQL.
 *
 * <p>Преимущества подхода:</p>
 * <ul>
 *   <li>1) Разовая операция на старте: инициализация каталога при запуске – не нужен «долгоживущий» сервис,
 *       достаточно один раз выполнить SQL-команду;</li>
 *   <li>2) Всё прозрачно: SQL-команда собрана в одном месте, легко сверяется со схемой/миграциями;</li>
 *   <li>3) Полный контроль записи: пишем напрямую в БД (UPSERT/DELETE) без участия механизмов JPA;</li>
 *   <li>4) Быстро и предсказуемо: batch + ON CONFLICT работают без накладных расходов ORM;</li>
 *   <li>5) Безопасно для связей: UPSERT по provider_code не меняет PK – внешние ключи не страдают;</li>
 *   <li>6) Просто тестировать: DAO легко проверить изолированно (например, через Testcontainers).</li>
 * </ul>
 */
@Repository
public class ProviderPassportSyncDaoPostgresAdapter implements ProviderPassportSyncDao {

    /* Конвертер Duration ↔ seconds */
    private static final DurationToSecondsConverter DUR2SEC = new DurationToSecondsConverter();

    /* Spring JdbcTemplate. */
    private final JdbcTemplate jdbc;

    /* Конструктор. */
    public ProviderPassportSyncDaoPostgresAdapter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Пакетное удаление провайдеров по набору уникальных кодов провайдеров.
     *
     * <p>Безопасно вызывать с пустой коллекцией – операция будет пропущена.</p>
     *
     * @param codes набор кодов провайдеров для удаления
     * @throws DataAccessException если БД вернула ошибку
     */
    @Override
    public void deleteByCodes(Collection<ProviderCode> codes) {
        if (codes == null || codes.isEmpty()) return;

        // SQL-команда
        final String sql = "DELETE FROM market_data_provider WHERE provider_code = ?";

        // Преобразуем коллекцию в массив – нужен индекс для BatchPreparedStatementSetter и фиксированный размер батча.
        // Предполагаем, что коды пришли из БД (или модели ProviderSnapshot) и уже нормализованы.
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
     * Пакетный UPSERT всех переданных снимков провайдеров.
     *
     * <p>Логика {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE SET ...}: если записи с таким
     * {@code provider_code} нет – вставка; если есть – обновление нужных полей из переданного {@code snapshot}.</p>
     * <p>Безопасно вызывать с пустой коллекцией – операция будет пропущена.</p>
     *
     * @param snapshots коллекция снимков ({@link ProviderSnapshot}) для вставки/обновления
     * @throws DataAccessException если БД вернула ошибку
     */
    @Override
    public void upsertAll(Collection<ProviderSnapshot> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) return;

        // Берём аудит-атрибуты из контекст-холдера
        final String actor = AuditContextHolder.actorOrFallback();
        final String via = AuditContextHolder.viaOrFallback();

        // SQL-команда
        final String sql = """
                INSERT INTO market_data_provider(
                  provider_code,
                  display_name,
                  delivery_mode,
                  access_method,
                  bulk_subscription,
                  min_update_interval_seconds,
                  version,
                  created_timestamp,
                  created_by,
                  created_via,
                  updated_timestamp,
                  updated_by,
                  updated_via
                ) VALUES (?, ?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                ON CONFLICT (provider_code) DO UPDATE SET
                  display_name = EXCLUDED.display_name,
                  delivery_mode = EXCLUDED.delivery_mode,
                  access_method = EXCLUDED.access_method,
                  bulk_subscription = EXCLUDED.bulk_subscription,
                  min_update_interval_seconds = EXCLUDED.min_update_interval_seconds,
                  version = market_data_provider.version + 1,
                  updated_timestamp = CURRENT_TIMESTAMP,
                  updated_by = EXCLUDED.updated_by,
                  updated_via = EXCLUDED.updated_via
                """;

        // Преобразуем коллекцию в массив – нужен индекс для BatchPreparedStatementSetter и фиксированный размер батча.
        ProviderSnapshot[] arr = snapshots.stream().filter(Objects::nonNull).toArray(ProviderSnapshot[]::new);

        // Пакетный UPSERT: берём снимок arr[i], привязываем параметры через bindUpsert(...), задаём размер батча.
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@org.springframework.lang.NonNull PreparedStatement ps, int i) throws SQLException {
                bindUpsert(ps, arr[i], actor, via);
            }

            @Override
            public int getBatchSize() {
                return arr.length;
            }
        });
    }

    /**
     * Привязка параметров для одной строки UPSERT (заполнение параметров "?" в SQL-команде конкретными значениями).
     */
    private void bindUpsert(PreparedStatement ps, ProviderSnapshot s, String actor, String via) throws SQLException {
        Objects.requireNonNull(s, "snapshot must not be null");
        Objects.requireNonNull(actor, "actor must not be null");
        Objects.requireNonNull(via, "via must not be null");
        // Примечание: иные проверки не обязательны – корректность данных гарантируется моделью ProviderSnapshot

        // 1) provider_code (натуральный ключ)
        ps.setString(1, s.code().value());

        // 2) passport.*
        ProviderPassport passport = s.passport();
        ps.setString(2, passport.displayName());         // <-- display_name
        ps.setString(3, passport.deliveryMode().name()); // <-- delivery_mode (EnumType.STRING)
        ps.setString(4, passport.accessMethod().name()); // <-- access_method (EnumType.STRING)
        ps.setBoolean(5, passport.bulkSubscription());   // <-- bulk_subscription

        // 3) policy.*  (Duration --> seconds через общий конвертер)
        ProviderPolicy policy = s.policy();
        Long seconds = DUR2SEC.convertToDatabaseColumn(policy.minUpdateInterval());
        ps.setLong(6, seconds);                   // <-- min_update_interval_seconds

        // 4) audit-атрибуты (actor/via). Используем одинаковые значения для created_* и updated_*.
        ps.setString(7, actor);                   // <-- created_by
        ps.setString(8, via);                     // <-- created_via
        ps.setString(9, actor);                   // <-- updated_by
        ps.setString(10, via);                    // <-- updated_via
    }
}
