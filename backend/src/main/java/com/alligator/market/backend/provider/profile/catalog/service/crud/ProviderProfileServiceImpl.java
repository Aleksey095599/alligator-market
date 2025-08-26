package com.alligator.market.backend.provider.profile.catalog.service.crud;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * Реализация сервиса профилей провайдеров рыночных данных.
 */
@Service
@RequiredArgsConstructor
public class ProviderProfileServiceImpl implements ProviderProfileService {

    private final ProviderProfileStorage storage;

    @Override
    public Map<Long, Profile> findAllActive() {
        return storage.findAllActive();
    }

    @Override
    public Map<Profile, ProfileStatus> findAllWithStatus() {
        return storage.findAllWithStatus();
    }
}
