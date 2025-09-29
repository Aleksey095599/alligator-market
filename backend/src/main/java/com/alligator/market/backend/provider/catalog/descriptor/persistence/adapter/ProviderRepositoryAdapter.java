package com.alligator.market.backend.provider.catalog.descriptor.persistence.adapter;

import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntityMapper;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorJpaRepository;
import com.alligator.market.domain.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Адаптер, реализующий порт доменного репозитория для дескрипторов через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderRepositoryAdapter implements ProviderRepository {

    private final DescriptorJpaRepository jpaRepository;
    private final DescriptorEntityMapper mapper;

}
