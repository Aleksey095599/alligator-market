package com.alligator.market.backend.provider.profile.catalog.persistence.adapter;

import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProfileEntity;
import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProfileEntityMapper;
import com.alligator.market.backend.provider.profile.catalog.persistence.jpa.ProfileJpaRepository;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Адаптер, реализующий доменный порт {@link ProfileRepository} через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProfileStorageAdapter implements ProfileRepository {

    private final ProfileJpaRepository jpaRepository;
    private final ProfileEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Profile> findAllActive() {
        return jpaRepository.findAllByProfileStatus(ProfileStatus.ACTIVE).stream()
                .collect(Collectors.toMap(
                        ProfileEntity::getId,
                        mapper::toDomain
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Profile, ProfileStatus> findAllWithStatus() {
        return jpaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        mapper::toDomain,
                        ProfileEntity::getProfileStatus
                ));
    }

    @Override
    public void saveAll(Collection<Profile> profiles) {
        var entities = profiles.stream()
                .map(mapper::toEntity)
                .toList();
        jpaRepository.saveAll(entities);
    }

    @Override
    public void updateStatus(Collection<Long> ids, ProfileStatus status) {
        var entities = jpaRepository.findAllById(ids);
        entities.forEach(e -> e.setProfileStatus(status));
        jpaRepository.saveAll(entities);
    }
}

