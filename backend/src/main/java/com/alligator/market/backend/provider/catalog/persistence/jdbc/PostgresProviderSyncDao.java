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
 * DAO (Data Access Object) для пакетного UPSERT/DELETE провайдеров в таблице {@code market_data_provider}.
 * Подходит только для базы данных (далее БД) PostgreSQL.
 *
 * <p><b>Назначение:</b>
 * <ul>
 *   <li>Пакетная загрузка "снимков провайдеров" {@link ProviderSnapshot} в базу данных;</li>
 *   <li>Атомарная операция «вставить или обновить» по натуральному ключу {@code provider_code} с помощью
 *       PostgreSQL-конструкции {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE ...};</li>
 *   <li>Удаление устаревших записей по набору кодов.</li>
 * </ul>
 *
 * <p><b>Преимущества подхода:</b>
 * <ul>
 *   <li><b>Разовая операция на старте.</b> Это инициализация каталога при запуске — не нужен «долгоживущий» сервис.
 *       Достаточно один раз выполнить продуманный SQL-команду.</li>
 *   <li><b>Всё прозрачно.</b> SQL-команда собрана в одном месте, легко сверяется со схемой/миграциями.</li>
 *   <li><b>Полный контроль записи.</b> Пишем напрямую в БД (UPSERT/DELETE), JPA остаётся read‑only (@Immutable).</li>
 *   <li><b>Быстро и предсказуемо.</b> Batch + ON CONFLICT работают без накладных расходов ORM.</li>
 *   <li><b>Безопасно для связей.</b> UPSERT по provider_code не меняет PK — внешние ключи не страдают.</li>
 *   <li><b>Просто тестировать.</b> DAO легко проверить изолированно (например, через Testcontainers).</li>
 * </ul>
 *
 * <p><b>Потокобезопасность:</b>
 * DAO без состояния; JdbcTemplate потокобезопасен. Методы допустимо вызывать из разных потоков,
 * но по смыслу синхронизация выполняется один раз при старте и целиком в @Transactional-сервисе.
 * В кластере запускайте синк только на «лидере» (ShedLock/PG advisory lock), чтобы избежать гонок.
 * UPSERT идемпотентен, удаление безопасно при одинаковом входном наборе кодов.
 *
 * <p><b>Ограничения:</b>
 * <ul>
 *   <li>БД — PostgreSQL;</li>
 *   <li>{@code provider_code} — уникальное ограничение в таблице {@code market_data_provider};</li>
 *   <li>Колонки в SQL-команде соответствуют реальной схеме {@code market_data_provider}. </li>
 * </ul>
 *
 * @see ProviderSnapshot
 */
@Repository
public class PostgresProviderSyncDao {

    // Spring JdbcTemplate: SQL/батчи через DataSource, управление ресурсами и перевод SQLException → DataAccessException.
    private final JdbcTemplate jdbc;

    // Конвертер Duration ↔ seconds
    private static final DurationToSecondsConverter DUR2SEC = new DurationToSecondsConverter();

    /* Конструктор. */
    public PostgresProviderSyncDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Пакетное удаление провайдеров по набору уникальных технических кодов.
     * <p>Безопасно вызывать с пустой коллекцией — операция будет пропущена.</p>
     *
     * @param codes набор кодов провайдеров ({@code provider_code}) для удаления
     * @throws DataAccessException если БД вернула ошибку
     */
    public void deleteByCodes(Collection<String> codes) {
        if (codes == null || codes.isEmpty()) return;

        // SQL-команда
        final String sql = "DELETE FROM market_data_provider WHERE provider_code = ?";

        // Преобразуем коллекцию в массив — нужен индекс для BatchPreparedStatementSetter и фиксированный размер батча.
        // Предполагаем, что коды пришли из БД (или модели ProviderSnapshot) и уже нормализованы.
        var arr = codes.stream().filter(Objects::nonNull).toArray(String[]::new);

        // Пакетно выполняем DELETE: привязываем provider_code по индексу и задаём размер батча.
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, arr[i]);
            }
            @Override public int getBatchSize() { return arr.length; }
        });
    }

    /**
     * Пакетный UPSERT всех переданных снимков провайдеров.
     * <p>
     * Логика {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE SET ...}: если записи с таким
     * {@code provider_code} нет — вставка; если есть — обновление нужных полей из переданного {@code snapshot}.
     * </p>
     * <p>Безопасно вызывать с пустой коллекцией — операция будет пропущена.</p>
     *
     * @param snapshots коллекция снимков ({@link ProviderSnapshot}) для вставки/обновления
     * @throws DataAccessException если БД вернула ошибку
     */
    public void upsertAll(Collection<ProviderSnapshot> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) return;

        // SQL-команда
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

        // Преобразуем коллекцию в массив — нужен индекс для BatchPreparedStatementSetter и фиксированный размер батча.
        var arr = snapshots.stream().filter(Objects::nonNull).toArray(ProviderSnapshot[]::new);

        // Пакетный UPSERT: берём снимок arr[i], привязываем параметры через bindUpsert(...), задаём размер батча.
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(PreparedStatement ps, int i) throws SQLException {
                bindUpsert(ps, arr[i]);
            }
            @Override public int getBatchSize() { return arr.length; }
        });
    }

    /**
     * Привязка параметров для одной строки UPSERT (заполнение параметров "?" в SQL-команде конкретными значениями).
     */
    private void bindUpsert(PreparedStatement ps, ProviderSnapshot s) throws SQLException {
        Objects.requireNonNull(s, "snapshot must not be null");
        // Иные проверки не обязательны — корректность данных гарантируется моделью ProviderSnapshot

        // 1) provider_code (натуральный ключ)
        ps.setString(1, s.code());

        // 2) descriptor.*
        var d = s.descriptor();
        ps.setString(2, d.displayName());         // display_name
        ps.setString(3, d.deliveryMode().name()); // delivery_mode (EnumType.STRING)
        ps.setString(4, d.accessMethod().name()); // access_method (EnumType.STRING)
        ps.setBoolean(5, d.bulkSubscription());   // bulk_subscription

        // 3) policy.*  (Duration → seconds через общий конвертер)
        var p = s.policy();
        Long seconds = DUR2SEC.convertToDatabaseColumn(p.minUpdateInterval());
        ps.setLong(6, seconds);                   // min_update_interval_seconds
    }
}
