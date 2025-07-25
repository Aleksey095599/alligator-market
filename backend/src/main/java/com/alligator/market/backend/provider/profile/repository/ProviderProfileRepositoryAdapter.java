package com.alligator.market.backend.provider.profile.repository;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.mapper.ProviderProfileMapper;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Адаптер реализующий доменный репозиторий {@link com.alligator.market.domain.provider.ProviderProfileRepository}
 * в контексте Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderProfileRepositoryAdapter implements com.alligator.market.domain.provider.ProviderProfileRepository {

    private final ProviderProfileJpaRepository jpaRepository;

    @Override
    public Map<ProviderProfile, Long> findAll() {
        return jpaRepository.findAll(Sort.by("providerCode")).stream()
                .collect(Collectors.toMap(
                        ProviderProfileMapper::toDomain,
                        ProviderProfileEntity::getId
                ));
    }
}
