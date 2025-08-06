package com.alligator.market.backend.provider.profile.catalog.repository;

import com.alligator.market.backend.provider.profile.catalog.entity.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.catalog.mapper.ProviderProfileMapper;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Адаптер реализующий доменную модель хранилища профилей провайдеров
 * {@link ProviderProfileStorage} в контексте Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderProfileStorageAdapter implements ProviderProfileStorage {

    private final ProviderProfileJpaRepository jpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<ProviderProfile, Long> findAllActive() {
        return jpaRepository.findAllByStatus(ProviderProfileStatus.ACTIVE).stream()
                .collect(Collectors.toMap(
                        ProviderProfileMapper::toDomain,
                        ProviderProfileEntity::getId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus() {
        return jpaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        ProviderProfileMapper::toDomain,
                        ProviderProfileEntity::getStatus
                ));
    }

    @Override
    public void saveAll(Collection<ProviderProfile> profiles) {
        var entities = profiles.stream()
                .map(p -> ProviderProfileMapper.toEntity(p, ProviderProfileStatus.ACTIVE))
                .toList();
        jpaRepository.saveAll(entities);
    }

    @Override
    public void updateStatus(Collection<Long> ids, ProviderProfileStatus status) {
        var entities = jpaRepository.findAllById(ids);
        entities.forEach(e -> e.setStatus(status));
        jpaRepository.saveAll(entities);
    }
}
