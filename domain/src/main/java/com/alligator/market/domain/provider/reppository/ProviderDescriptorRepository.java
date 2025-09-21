package com.alligator.market.domain.provider.reppository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.List;

/**
 * Порт репозитория дескрипторов провайдеров.
 */
public interface ProviderDescriptorRepository {

    /** Загрузить все дескрипторы провайдеров. */
    List<ProviderDescriptor> findAll();

    /** Полностью очистить таблицу дескрипторов. */
    void deleteAll();

    /** Пакетно сохранить список дескрипторов. */
    void saveAll(List<ProviderDescriptor> descriptors);
}
