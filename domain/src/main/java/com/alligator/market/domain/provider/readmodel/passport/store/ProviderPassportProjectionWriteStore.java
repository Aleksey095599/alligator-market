package com.alligator.market.domain.provider.readmodel.passport.store;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Map;
import java.util.Set;

/**
 * Write-порт проекции паспортов провайдеров.
 *
 * <p>Назначение: поддерживать materialized view (read model) паспортов провайдеров в согласованном состоянии
 * относительно реестра провайдеров {@link com.alligator.market.domain.provider.registry.ProviderRegistry}.</p>
 *
 * <p>Порт описывает только контракт операций. Конкретная технология хранения/обновления
 * (JDBC/JPA и т.п.) — ответственность backend-адаптеров.</p>
 *
 * <p>Типовой сценарий синхронизации:</p>
 * <ul>
 *   <li>{@link #deleteAllExcept(Set)} и затем {@link #upsertAll(Map)}.</li>
 * </ul>
 *
 * <p>Примечания:</p>
 * <ul>
 *   <li>Рекомендуется выполнять последовательность синхронизации в одной транзакции
 *       (транзакционность обеспечивает вызывающая сторона).</li>
 *   <li>При нарушении требований к аргументам реализация вправе выбросить {@link IllegalArgumentException}.</li>
 * </ul>
 */
public interface ProviderPassportProjectionWriteStore {

    /**
     * Удалить все записи проекции, кроме указанных кодов провайдеров.
     *
     * <p>Семантика: после успешного выполнения в проекции остаются записи только для кодов из {@code activeCodes}.</p>
     *
     * <p>Требования:</p>
     * <ul>
     *   <li>{@code activeCodes} не {@code null};</li>
     *   <li>{@code activeCodes} не пуст;</li>
     *   <li>{@code activeCodes} не содержит {@code null}-элементов;</li>
     *   <li>Операция идемпотентна относительно множества {@code activeCodes} (порядок элементов не важен).</li>
     * </ul>
     *
     * @param activeCodes коды активных провайдеров
     */
    void deleteAllExcept(Set<ProviderCode> activeCodes);

    /**
     * Вставить или обновить паспорта провайдеров в проекции.
     *
     * <p>Семантика: для каждого {@link ProviderCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * <p>Требования:</p>
     * <ul>
     *   <li>{@code passports} не {@code null};</li>
     *   <li>{@code passports} не содержит {@code null}-ключей и {@code null}-значений;</li>
     *   <li>Операция идемпотентна относительно отображения {@code ProviderCode → ProviderPassport}
     *       (повторный вызов с теми же парами приводит к тому же результату по содержательным полям паспорта).</li>
     * </ul>
     *
     * <p>Пустая карта трактуется как no-op.</p>
     *
     * @param passports карта "код провайдера → паспорт провайдера"
     */
    void upsertAll(Map<ProviderCode, ProviderPassport> passports);
}
