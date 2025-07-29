package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * Реализация контракта сервиса {@link ProviderProfileService}.
 */
@Service
@RequiredArgsConstructor
public class ProviderProfileServiceImpl implements ProviderProfileService {

    private final ProviderProfileStorage storage;

    /** Возвращает все профили провайдеров со статусом ACTIVE. */
    @Override
    public Map<ProviderProfile, Long> findAllActive() {
        return storage.findAllActive();
    }
}
