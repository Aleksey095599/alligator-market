package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Порт репозитория настроек провайдеров.
 */
public interface ProviderSettingsRepository {

    /** Получить карту настроек провайдеров, индексированную по коду провайдера. */
    Map<String, ProviderSettings> findAll();

    /** Полностью очистить таблицу настроек (админ/служебная операция). */
    void deleteAll();

    /** Удалить настройки по списку кодов провайдеров. */
    void deleteAllByProviderCodes(Collection<String> providerCodes);

    /** INSERT после предварительного удаления; не upsert. Дубликаты providerCode → ошибка UNIQUE. */
    void insertAll(Map<String, ProviderSettings> settings);
}
