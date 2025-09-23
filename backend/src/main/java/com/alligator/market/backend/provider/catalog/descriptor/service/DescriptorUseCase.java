package com.alligator.market.backend.provider.catalog.descriptor.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.List;

/**
 * Application-сервис (use case) для операций с дескрипторами.
 */
public interface DescriptorUseCase {

    /** Вернуть все дескрипторы. */
    List<ProviderDescriptor> getAll();
}
