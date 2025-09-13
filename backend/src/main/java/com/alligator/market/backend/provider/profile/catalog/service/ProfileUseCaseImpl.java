package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;
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
    public Map<Long, ProviderStaticInfo> findAllActive() {
        return repository.findAllActive();
    }

    @Override
    public Map<ProviderStaticInfo, ProfileStatus> findAllWithStatus() {
        return repository.findAllWithStatus();
    }
}

