package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Map;

/**
 * Порт репозитория дескрипторов провайдеров.
 */
public interface ProviderDescriptorRepository {

    /** Вернуть все дескрипторы с индексацией по коду провайдера. */
    Map<String, ProviderDescriptor> findAll();

    /** Удалить все дескрипторы. */
    void deleteAll();
}
