package com.alligator.market.backend.provider.profile.catalog.service.crud;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
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
    public Map<Long, Profile> findAllActive() {
        return repository.findAllActive();
    }

    @Override
    public Map<Profile, ProfileStatus> findAllWithStatus() {
        return repository.findAllWithStatus();
    }
}

