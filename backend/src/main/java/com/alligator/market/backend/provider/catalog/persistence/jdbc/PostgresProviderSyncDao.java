package com.alligator.market.backend.provider.catalog.persistence.jdbc;

import com.alligator.market.backend.common.persistence.jpa.converter.DurationToSecondsConverter;
import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * DAO (Data Access Object) для пакетного UPSERT/DELETE провайдеров в таблице {@code market_data_provider} (PostgreSQL).
 *
 * <p><b>Задача и назначение:</b>
 * <ul>
 *   <li>Пакетная загрузка снимков провайдеров {@link ProviderSnapshot} в базу данных;</li>
 *   <li>Атомарная операция «вставить или обновить» по натуральному ключу {@code provider_code} с помощью
 *       PostgreSQL-конструкции {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE ...};</li>
 *   <li>Удаление устаревших записей по набору кодов.</li>
 * </ul>
 *
 * <p><b>Преимущества подхода:</b>
 * <ul>
 *   <li><i>Стабильность PK (id):</i> UPSERT обновляет существующую строку — внешние ключи не ломаются.</li>
 *   <li><i>Простота и атомарность:</i> одна SQL-операция покрывает «создать/обновить».</li>
 *   <li><i>Совместимость с JPA-immutability:</i> сущность {@code ProviderEntity} помечена {@code @Immutable};
 *          мы пишем напрямую SQL, не полагаясь на ORM-апдейты. Имена колонок соответствуют entity. </li>
 * </ul>
 *
 * <p><b>Системные предпосылки:</b>
 * <ul>
 *   <li>База данных — PostgreSQL;</li>
 *   <li>{@code provider_code} — уникальный констрейнт в таблице {@code market_data_provider};</li>
 *   <li>Колонки в SQL запросе соответствуют реальной схеме {@code market_data_provider}. </li>
 * </ul>
 */
@Repository
public class PostgresProviderSyncDao {

    private final JdbcTemplate jdbc;

    // Конвертер Duration ↔ seconds
    private static final DurationToSecondsConverter DUR2SEC = new DurationToSecondsConverter();

    /* Конструктор. */
    public PostgresProviderSyncDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Пакетное удаление провайдеров по набору технических кодов.
     * <p>Безопасно вызывать с пустой коллекцией — операция будет пропущена.</p>
     *
     * @param codes набор кодов провайдеров ({@code provider_code}) для удаления
     * @throws DataAccessException если БД вернула ошибку
     */
    public void deleteByCodes(Collection<String> codes) {
        if (codes == null || codes.isEmpty()) return;

        final String sql = "DELETE FROM market_data_provider WHERE provider_code = ?";

        // Предполагаем, что коды пришли из БД / ProviderSnapshot и уже нормализованы.
        var arr = codes.stream()
                .filter(Objects::nonNull)
                .toArray(String[]::new);

        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, arr[i]); // provider_code
            }
            @Override public int getBatchSize() { return arr.length; }
        });
    }

    /**
     * Пакетный UPSERT всех переданных снимков провайдеров.
     * <p>
     * Логика {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE SET ...}: если записи с таким
     * {@code provider_code} нет — вставка; если есть — обновление указанных полей.
     * </p>
     * <p>Безопасно вызывать с пустой коллекцией — операция будет пропущена.</p>
     *
     * @param snapshots коллекция снимков ({@link ProviderSnapshot}) для вставки/обновления
     * @throws DataAccessException если БД вернула ошибку
     */
    public void upsertAll(Collection<ProviderSnapshot> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) return;

        final String sql = """
            INSERT INTO market_data_provider(
              provider_code,
              display_name,
              delivery_mode,
              access_method,
              bulk_subscription,
              min_update_interval_seconds
            ) VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (provider_code) DO UPDATE SET
              display_name = EXCLUDED.display_name,
              delivery_mode = EXCLUDED.delivery_mode,
              access_method = EXCLUDED.access_method,
              bulk_subscription = EXCLUDED.bulk_subscription,
              min_update_interval_seconds = EXCLUDED.min_update_interval_seconds
            """;

        var list = snapshots.stream().filter(Objects::nonNull).toArray(ProviderSnapshot[]::new);

        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(PreparedStatement ps, int i) throws SQLException {
                bindUpsert(ps, list[i]); // ← Связываем параметры одной строки UPSERT
            }
            @Override public int getBatchSize() { return list.length; }
        });
    }

    /**
     * Привязка параметров для одной строки UPSERT.
     * Ожидается, что {@link ProviderSnapshot} уже прошёл валидацию/нормализацию.
     */
    private void bindUpsert(PreparedStatement ps, ProviderSnapshot s) throws SQLException {
        // Снимок не должен быть null — этого достаточно (остальное гарантирует сам snapshot)
        Objects.requireNonNull(s, "snapshot must not be null");

        // 1) provider_code (натуральный ключ) — уже UPPER и проверен в ProviderSnapshot
        ps.setString(1, s.code());

        // 2) descriptor.*
        var d = s.descriptor();
        ps.setString(2, d.displayName());         // display_name
        ps.setString(3, d.deliveryMode().name()); // delivery_mode (EnumType.STRING)
        ps.setString(4, d.accessMethod().name()); // access_method (EnumType.STRING)
        ps.setBoolean(5, d.bulkSubscription());   // bulk_subscription

        // 3) policy.*  (Duration → seconds через общий конвертер; NOT NULL гарантирует snapshot)
        var p = s.policy();
        Long seconds = DUR2SEC.convertToDatabaseColumn(p.minUpdateInterval());
        ps.setLong(6, seconds);                   // min_update_interval_seconds
    }
}
