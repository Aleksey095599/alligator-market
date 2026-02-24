package com.alligator.market.domain.provider.readmodel.passport.store;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Collection;
import java.util.Map;

/**
 * Write-хранилище (порт) проекции паспортов провайдеров.
 *
 * <p>Назначение: поддерживать materialized view (read model) паспортов провайдеров в согласованном состоянии
 * относительно реестра провайдеров рыночных данных {@code ProviderRegistry}.</p>
 */
public interface ProviderPassportProjectionWriteStore {

    /**
     * Удалить все записи из проекции.
     *
     * <p>Используется, когда в реестре нет активных провайдеров.</p>
     */
    void deleteAll();

    /**
     * Удалить записи из проекции для всех провайдеров, кроме указанных.
     *
     * <p>Используется для синхронизации проекции с реестром без предварительного чтения кодов из хранилища.</p>
     *
     * @param activeCodes коды активных провайдеров (не {@code null}, без {@code null}-элементов)
     */
    void deleteAllExcept(Collection<ProviderCode> activeCodes);

    /**
     * Вставить или обновить паспорта провайдеров в проекции.
     *
     * <p>Требования к реализации:</p>
     * <ul>
     *   <li>операция должна быть идемпотентной на уровне натурального ключа {@code provider_code};</li>
     *   <li>все ключи и значения в {@code passports} должны быть не {@code null}.</li>
     * </ul>
     *
     * @param passports карта "код провайдера → паспорт провайдера" (не {@code null}, без {@code null}-ключей/значений)
     */
    void upsertAll(Map<ProviderCode, ProviderPassport> passports);
}
