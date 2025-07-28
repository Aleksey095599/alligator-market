package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


/**
 * Реализация интерфейса сервиса {@link ProviderProfileService}.
 */
@Service
@RequiredArgsConstructor
public class ProviderProfileServiceImpl implements ProviderProfileService {

    private final ProviderProfileRepository repository;

    /** Возвращает все профили провайдеров со статусом ACTIVE */
    @Override
    public Map<ProviderProfile, Long> findAllActive() {
        return repository.findAllActive();
    }

    /** Сохраняет заданную коллекцию профилей со статусом ACTIVE */
    @Override
    public void saveAll(Collection<ProviderProfile> profiles) {
        repository.saveAll(profiles);
    }
}
