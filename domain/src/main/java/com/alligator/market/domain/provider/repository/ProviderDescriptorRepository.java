package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Map;

/**
 * Порт репозитория провайдеров рыночных данных.
 */
public interface ProviderDescriptorRepository {

    /** Вернуть все дескрипторы с индексацией по коду провайдера. */
    Map<String, ProviderDescriptor> findAll();
}
