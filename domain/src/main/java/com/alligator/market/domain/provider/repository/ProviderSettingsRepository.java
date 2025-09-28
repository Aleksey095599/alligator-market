package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;

import java.util.Collection;
import java.util.List;

/**
 * Порт репозитория настроек провайдеров.
 */
public interface ProviderSettingsRepository {

    /** Получить все настройки провайдеров. */
    List<ProviderSettings> findAll();

    /** Полностью очистить таблицу настроек (админ/служебная операция). */
    void deleteAll();

    /** Удалить настройки по списку кодов провайдеров. */
    void deleteAllByProviderCodes(Collection<String> providerCodes);

    /** INSERT после предварительного удаления; не upsert. Дубликаты providerCode → ошибка UNIQUE. */
    void insertAll(List<ProviderSettings> settings);
}
