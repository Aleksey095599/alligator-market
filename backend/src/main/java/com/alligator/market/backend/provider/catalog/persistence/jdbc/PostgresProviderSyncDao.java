package com.alligator.market.backend.provider.catalog.persistence.jdbc;

import com.alligator.market.backend.common.persistence.jpa.converter.DurationToSecondsConverter;
import com.alligator.market.backend.provider.catalog.persistence.jpa.ProviderEntity;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * DAO (Data Access Object) для пакетного UPSERT/DELETE провайдеров в таблице {@code market_data_provider} (PostgreSQL).
 *
 * <p><b>Задача и назначение:</b>
 * <ul>
 *   <li>Пакетная загрузка снимков провайдеров {@link ProviderSnapshot} из контекста приложения в БД;</li>
 *   <li>Атомарная операция «вставить или обновить» по натуральному ключу {@code provider_code}
 *       — с помощью PostgreSQL-конструкции {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE ...};</li>
 *   <li>Удаление устаревших записей по набору кодов.</li>
 * </ul>
 *
 * <p><b>Почему это удобно и безопасно:</b>
 * <ul>
 *   <li><i>Стабильность PK (id):</i> при UPSERT обновляется существующая строка — внешние ключи (если появятся) не ломаются.</li>
 *   <li><i>Простота и атомарность:</i> одна SQL-операция покрывает «создать/обновить» — меньше ветвлений и гоночных условий.</li>
 *   <li><i>Совместимость с JPA-immutability:</i> JPA-сущности помечены {@code @Immutable}; мы не полагаемся на ORM-апдейты,
 *       пишем напрямую SQL — это соответствует стратегии синхронизации на старте.</li>
 * </ul>
 *
 * <p><b>Точки интеграции:</b>
 * <ul>
 *   <li>Вызов методов этого DAO выполняется из доменного сервиса синхронизации {@link ProviderSynchronizer}
 *       под транзакцией {@code @Transactional} — весь шаг синка логически атомарен.</li>
 *   <li>Названия колонок соответствуют JPA-схеме {@link ProviderEntity} — это критично для корректности SQL.</li>
 * </ul>
 *
 * <p><b>Ограничения и допущения:</b>
 * <ul>
 *   <li>Система управления БД — PostgreSQL (используется {@code ON CONFLICT ... DO UPDATE});</li>
 *   <li>{@code provider_code} уникален (есть уникальный индекс/констрейнт) — требуется для UPSERT;</li>
 *   <li>Значения enum сохраняются как {@code EnumType.STRING} (используем {@code name()});</li>
 *   <li>{@code min_update_interval_seconds} — целочисленное число секунд (используем конвертер {@link DurationToSecondsConverter}).</li>
 * </ul>
 *
 * <p><b>Порядок типичного применения:</b>
 * <ol>
 *   <li>Считать снимки из контекста ({@link ProviderSnapshot});</li>
 *   <li>Удалить устаревшие записи: {@link #deleteByCodes(Collection)};</li>
 *   <li>Выполнить UPSERT всех актуальных: {@link #upsertAll(Collection)}.</li>
 * </ol>
 */
@Repository // ← Включаем перевод SQL-исключений в DataAccessException и видимость как Spring-DAO
public class PostgresProviderSyncDao {

    private final JdbcTemplate jdbc;

    public PostgresProviderSyncDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /* ---------- Публичные методы ---------- */

    /**
     * Пакетное удаление провайдеров по набору технических кодов.
     * <p>Безопасно вызывать с пустой коллекцией — операция будет пропущена.</p>
     *
     * @param codes набор кодов провайдеров ({@code provider_code}) для удаления
     * @throws org.springframework.dao.DataAccessException если БД вернула ошибку
     */
    public void deleteByCodes(Collection<String> codes) {
        if (codes == null || codes.isEmpty()) return; // ← Ничего удалять

        final String sql = "DELETE FROM market_data_provider WHERE provider_code = ?";
        var arr = codes.stream()
                .filter(Objects::nonNull)
                .map(c -> c.toUpperCase(Locale.ROOT)) // ← Нормализация на всякий случай (БД всё равно принудит UPPER)
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
     * Логика {@code INSERT ... ON CONFLICT (provider_code) DO UPDATE SET ...}: если запись с таким
     * {@code provider_code} отсутствует — будет вставка; если присутствует — произойдёт обновление указанных полей.
     * </p>
     * <p>Безопасно вызывать с пустой коллекцией — операция будет пропущена.</p>
     *
     * @param snapshots коллекция снимков ({@link ProviderSnapshot}) для вставки/обновления
     * @throws org.springframework.dao.DataAccessException если БД вернула ошибку
     */
    public void upsertAll(Collection<ProviderSnapshot> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) return; // ← Нечего апсертить

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

        var list = snapshots.stream()
                .filter(Objects::nonNull)
                .toArray(ProviderSnapshot[]::new);

        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override public void setValues(PreparedStatement ps, int i) throws SQLException {
                bindUpsert(ps, list[i]); // ← Проставляем параметры одной строки UPSERT
            }
            @Override public int getBatchSize() { return list.length; }
        });
    }

    /* ---------- Вспомогательные методы привязки параметров ---------- */

    /**
     * Привязка параметров для одной строки UPSERT.
     *
     * @param ps  подготовленный стейтмент
     * @param s   снимок провайдера (code + descriptor + policy)
     */
    private void bindUpsert(PreparedStatement ps, ProviderSnapshot s) throws SQLException {
        // ↓↓ Fail-fast проверки входных данных (защита от NPE/пустых значений, понятные сообщения)
        Objects.requireNonNull(s, "snapshot must not be null");
        final String code = Objects.requireNonNull(s.code(), "provider code must not be null").toUpperCase(Locale.ROOT);
        final ProviderDescriptor d = Objects.requireNonNull(s.descriptor(), "descriptor must not be null");
        final ProviderPolicy     p = Objects.requireNonNull(s.policy(), "policy must not be null");
        Objects.requireNonNull(d.displayName(),  "descriptor.displayName must not be null");
        Objects.requireNonNull(d.deliveryMode(), "descriptor.deliveryMode must not be null");
        Objects.requireNonNull(d.accessMethod(), "descriptor.accessMethod must not be null");
        Objects.requireNonNull(p.minUpdateInterval(), "policy.minUpdateInterval must not be null");

        // 1) provider_code (натуральный ключ)
        ps.setString(1, code);

        // 2) descriptor.*
        ps.setString(2, d.displayName());           // display_name
        ps.setString(3, d.deliveryMode().name());   // delivery_mode (EnumType.STRING)
        ps.setString(4, d.accessMethod().name());   // access_method (EnumType.STRING)
        ps.setBoolean(5, d.bulkSubscription());     // bulk_subscription

        // 3) policy.*
        ps.setLong(6, seconds(p.minUpdateInterval()));   // min_update_interval_seconds
    }

    /** Конвертирует {@link Duration} в целые секунды (тот же формат, что в колонке БД). */
    private static long seconds(Duration d) {
        return d.getSeconds(); // ← Бросит NPE, если d == null (мы проверяем выше)
    }
}
