package com.alligator.market.backend.provider.profile.catalog.persistence.adapter;

import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProviderProfileJpaRepository;
import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProviderProfileEntityMapper;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Адаптер порта репозитория профилей провайдеров рыночных данных на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderProfileStorageAdapter implements ProviderProfileStorage {

    private final ProviderProfileJpaRepository jpaRepository;
    private final ProviderProfileEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Profile> findAllActive() {
        return jpaRepository.findAllByStatus(ProfileStatus.ACTIVE).stream()
                .collect(Collectors.toMap(
                        ProviderProfileEntity::getId,
                        mapper::toDomain
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Profile, ProfileStatus> findAllWithStatus() {
        return jpaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        mapper::toDomain,
                        ProviderProfileEntity::getStatus
                ));
    }

    @Override
    public void saveAll(Collection<Profile> profiles) {
        var entities = profiles.stream()
                .map(p -> mapper.toEntity(p, ProfileStatus.ACTIVE))
                .toList();
        jpaRepository.saveAll(entities);
    }

    @Override
    public void updateStatus(Collection<Long> ids, ProfileStatus status) {
        var entities = jpaRepository.findAllById(ids);
        entities.forEach(e -> e.setStatus(status));
        jpaRepository.saveAll(entities);
    }
}
