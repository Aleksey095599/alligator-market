package com.alligator.market.backend.provider.catalog.descriptor.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Реализация сервиса {@link DescriptorUseCase}.
 */
@Service
@RequiredArgsConstructor
public class DescriptorUseCaseImpl implements DescriptorUseCase {

    private final ProfileRepository repository;

    @Override
    public Map<Long, ProviderDescriptor> findAllActive() {
        return repository.findAllActive();
    }
}

