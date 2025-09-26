package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Collection;
import java.util.List;

/**
 * Порт репозитория дескрипторов провайдеров.
 */
public interface ProviderDescriptorRepository {

    /** Получить все дескрипторы провайдеров. */
    List<ProviderDescriptor> findAll();

    /** Полностью очистить таблицу дескрипторов (админ/служебная операция). */
    void deleteAll();

    /** Удалить дескрипторы по списку кодов провайдеров. */
    void deleteAllByProviderCodes(Collection<String> providerCodes);

    /**
     * Пакетно сохранить список дескрипторов.
     * Семантика: UPSERT по providerCode (новые вставляются, существующие перезаписываются).
     */
    void saveAll(List<ProviderDescriptor> descriptors);
}
