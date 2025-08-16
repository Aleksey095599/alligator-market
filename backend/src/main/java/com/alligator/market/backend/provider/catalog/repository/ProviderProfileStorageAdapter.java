package com.alligator.market.backend.provider.catalog.repository;

import com.alligator.market.backend.provider.catalog.jpa.ProviderEntity;
import com.alligator.market.backend.provider.catalog.jpa.ProviderProfileJpaRepository;
import com.alligator.market.backend.provider.catalog.jpa.ProviderEntityMapper;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.catalog.ProviderProfileStorage;
import com.alligator.market.domain.provider.model.ProviderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Адаптер хранилища профилей провайдеров рыночных данных на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderProfileStorageAdapter implements ProviderProfileStorage {

    private final ProviderProfileJpaRepository jpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<Long, ProviderProfile> findAllActive() {
        return jpaRepository.findAllByStatus(ProviderStatus.ACTIVE).stream()
                .collect(Collectors.toMap(
                        ProviderEntity::getId,
                        ProviderEntityMapper::toDomain
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<ProviderProfile, ProviderStatus> findAllWithStatus() {
        return jpaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        ProviderEntityMapper::toDomain,
                        ProviderEntity::getStatus
                ));
    }

    @Override
    public void saveAll(Collection<ProviderProfile> profiles) {
        var entities = profiles.stream()
                .map(p -> ProviderEntityMapper.toEntity(p, ProviderStatus.ACTIVE))
                .toList();
        jpaRepository.saveAll(entities);
    }

    @Override
    public void updateStatus(Collection<Long> ids, ProviderStatus status) {
        var entities = jpaRepository.findAllById(ids);
        entities.forEach(e -> e.setStatus(status));
        jpaRepository.saveAll(entities);
    }
}
