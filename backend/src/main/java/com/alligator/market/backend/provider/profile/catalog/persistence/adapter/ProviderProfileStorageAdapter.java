package com.alligator.market.backend.provider.profile.catalog.persistence.adapter;

import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProviderProfileJpaRepository;
import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProviderProfileEntityMapper;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.contract.ProviderProfileStorage;
import com.alligator.market.domain.provider.model.ProviderProfileStatus;
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
    private final ProviderProfileEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Map<Long, ProviderProfile> findAllActive() {
        return jpaRepository.findAllByStatus(ProviderProfileStatus.ACTIVE).stream()
                .collect(Collectors.toMap(
                        ProviderProfileEntity::getId,
                        mapper::toDomain
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus() {
        return jpaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        mapper::toDomain,
                        ProviderProfileEntity::getStatus
                ));
    }

    @Override
    public void saveAll(Collection<ProviderProfile> profiles) {
        var entities = profiles.stream()
                .map(p -> mapper.toEntity(p, ProviderProfileStatus.ACTIVE))
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
