package com.alligator.market.backend.provider.profile.catalog.service.crud;

import com.alligator.market.domain.provider.model.profile.ProviderProfile;
import com.alligator.market.domain.provider.model.ProviderStatus;
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
    public Map<ProviderProfile, ProviderStatus> findAllWithStatus() {
        return storage.findAllWithStatus();
    }
}
