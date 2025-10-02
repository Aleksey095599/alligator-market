package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Map;
import java.util.Set;

/**
 * Порт репозитория дескрипторов провайдеров.
 */
public interface ProviderDescriptorRepository {

    /** Вернуть все дескрипторы с индексацией по коду провайдера. */
    Map<String, ProviderDescriptor> findAll();

    /** Удалить все дескрипторы. */
    void deleteAll();

    /** Удалить дескрипторы по переданным кодам провайдеров. */
    void deleteAllByProviderCodes(Set<String> providerCodes);

    /** Выполнить пакетную вставку дескрипторов. */
    void insertAll(Map<String, ProviderDescriptor> descriptors);
}
