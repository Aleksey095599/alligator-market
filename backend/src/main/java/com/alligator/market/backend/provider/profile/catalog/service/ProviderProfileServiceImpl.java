package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import com.alligator.market.domain.provider.sync.model.ProviderProfileStatus;
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
    public Map<Long, ProviderProfile> findAllActive() {
        return storage.findAllActive();
    }

    @Override
    public Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus() {
        return storage.findAllWithStatus();
    }
}
