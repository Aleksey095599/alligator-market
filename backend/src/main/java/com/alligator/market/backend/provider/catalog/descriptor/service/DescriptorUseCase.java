package com.alligator.market.backend.provider.catalog.descriptor.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Map;

/**
 * Application-сервис (use case) для операций с дескрипторами.
 */
public interface DescriptorUseCase {

    /** Вернуть карту всех дескрипторов, индексированную по коду провайдера. */
    Map<String, ProviderDescriptor> getAll();
}
