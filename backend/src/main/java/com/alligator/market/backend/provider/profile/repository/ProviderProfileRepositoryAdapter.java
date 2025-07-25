package com.alligator.market.backend.provider.profile.repository;

import com.alligator.market.backend.provider.profile.mapper.ProviderProfileMapper;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Адаптер реализующий доменный репозиторий {@link com.alligator.market.domain.provider.ProviderProfileRepository}
 * в контексте Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderProfileRepositoryAdapter implements com.alligator.market.domain.provider.ProviderProfileRepository {

    private final ProviderProfileJpaRepository jpaRepository;

    @Override
    public List<ProviderProfile> findAll() {
        return jpaRepository.findAll(Sort.by("providerCode")).stream()
                .map(ProviderProfileMapper::toDomain)
                .toList();
    }
}
