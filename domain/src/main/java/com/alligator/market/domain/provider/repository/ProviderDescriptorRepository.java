package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Collection;
import java.util.List;

/**
 * Порт репозитория дескрипторов провайдеров.
 */
public interface ProviderDescriptorRepository {

    /** Загрузить все дескрипторы провайдеров. */
    List<ProviderDescriptor> findAll();

    /** Полностью очистить таблицу дескрипторов. */
    void deleteAll();

    /** Удалить дескрипторы по списку кодов провайдеров. */
    void deleteAllByProviderCodes(Collection<String> providerCodes);

    /** Пакетно сохранить список дескрипторов. */
    void saveAll(List<ProviderDescriptor> descriptors);
}
