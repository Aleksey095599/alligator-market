package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Реализация сервиса {@link ProfileUseCase}.
 */
@Service
@RequiredArgsConstructor
public class ProfileUseCaseImpl implements ProfileUseCase {

    private final ProfileRepository repository;

    @Override
    public Map<Long, ProviderDescriptor> findAllActive() {
        return repository.findAllActive();
    }
}

