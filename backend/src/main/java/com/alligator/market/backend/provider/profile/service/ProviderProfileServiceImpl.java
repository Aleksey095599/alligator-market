package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.repository.ProviderProfileJpaRepository;
import com.alligator.market.backend.provider.profile.mapper.ProviderProfileMapper;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Реализация интерфейса сервиса {@link ProviderProfileService}.
 */
@Service
@RequiredArgsConstructor
public class ProviderProfileServiceImpl implements ProviderProfileService {

    private final ProviderProfileJpaRepository repository;

    /** Возвращает все профили провайдеров со статусом ACTIVE */
    @Override
    public Map<ProviderProfile, Long> findAllActive() {
        return repository.findAllByStatus(ProviderProfileStatus.ACTIVE).stream()
                .collect(Collectors.toMap(
                        ProviderProfileMapper::toDomain,
                        ProviderProfileEntity::getId
                ));
    }

    /** Сохраняет заданную коллекцию профилей */
    @Override
    public void saveAll(Collection<ProviderProfileEntity> entities) {
        repository.saveAll(entities);
    }
}
