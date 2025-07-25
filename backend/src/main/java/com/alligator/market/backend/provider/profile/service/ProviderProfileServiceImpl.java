package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.repository.ProviderProfileJpaRepository;
import com.alligator.market.backend.provider.profile.mapper.ProviderProfileMapper;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


/**
 * Реализация интерфейса сервиса {@link ProviderProfileService}.
 */
@Service
@RequiredArgsConstructor
public class ProviderProfileServiceImpl implements ProviderProfileService {

    private final ProviderProfileJpaRepository repository;

    /** Возвращает все профили провайдеров */
    @Override
    public List<ProviderProfile> findAll() {
        return repository.findAll().stream()
                .map(ProviderProfileMapper::toDomain)
                .toList();
    }

    /** Сохраняет заданную коллекцию профилей */
    @Override
    public void saveAll(Collection<ProviderProfileEntity> entities) {
        repository.saveAll(entities);
    }
}