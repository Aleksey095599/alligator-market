package com.alligator.market.backend.provider.catalog.info.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Map;

/**
 * Application-сервис (use case) для операций профилями провайдеров.
 */
public interface DescriptorUseCase {

    /** Вернуть все . */
    Map<Long, ProviderDescriptor> findAllActive();
}
