package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Реализация сервиса профилей провайдеров рыночных данных.
 */
@Service
@RequiredArgsConstructor
public class ProviderUseCaseImpl implements ProviderUseCase {

    private final ProfileRepository repository;

    @Override
    public Map<Long, Profile> findAllActive() {
        return repository.findAllActive();
    }

    @Override
    public Map<Profile, ProfileStatus> findAllWithStatus() {
        return repository.findAllWithStatus();
    }
}

