package com.alligator.market.backend.provider.profile.repository;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.mapper.ProviderProfileMapper;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileRepository;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Адаптер реализующий доменный репозиторий {@link ProviderProfileRepository}
 * в контексте Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderProfileRepositoryAdapter implements ProviderProfileRepository {

    private final ProviderProfileJpaRepository jpaRepository;

    @Override
    public Map<ProviderProfile, Long> findAllActive() {
        return jpaRepository.findAllByStatus(ProviderProfileStatus.ACTIVE).stream()
                .sorted((o1, o2) -> o1.getProviderCode().compareTo(o2.getProviderCode()))
                .collect(Collectors.toMap(
                        ProviderProfileMapper::toDomain,
                        ProviderProfileEntity::getId
                ));
    }
}
