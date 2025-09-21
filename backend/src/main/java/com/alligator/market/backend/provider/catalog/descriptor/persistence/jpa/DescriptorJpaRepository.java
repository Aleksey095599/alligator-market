package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA-репозиторий дескрипторов.
 */
public interface DescriptorJpaRepository extends JpaRepository<DescriptorEntity, Long> {

    /** Найти дескриптор по коду провайдера. */
    List<DescriptorEntity> findByProviderCode(String providerCode);
}

