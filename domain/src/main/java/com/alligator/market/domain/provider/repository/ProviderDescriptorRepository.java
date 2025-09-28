package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Collection;
import java.util.Map;

/**
 * Порт репозитория дескрипторов провайдеров.
 */
public interface ProviderDescriptorRepository {

    /** Получить карту дескрипторов провайдеров, индексированную по коду провайдера. */
    Map<String, ProviderDescriptor> findAll();

    /** Полностью очистить таблицу дескрипторов (админ/служебная операция). */
    void deleteAll();

    /** Удалить дескрипторы по списку кодов провайдеров. */
    void deleteAllByProviderCodes(Collection<String> providerCodes);

    /** INSERT после предварительного удаления (не UPSERT!). Дубликаты providerCode → ошибка UNIQUE. */
    void insertAll(Map<String, ProviderDescriptor> descriptors);
}
