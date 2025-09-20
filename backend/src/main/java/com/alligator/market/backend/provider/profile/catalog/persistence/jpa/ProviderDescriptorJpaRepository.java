package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA-репозиторий дескрипторов провайдеров.
 */
public interface ProviderDescriptorJpaRepository extends JpaRepository<ProviderDescriptorEntity, Long> {

    /** Найти дескриптор по коду провайдера. */
    List<ProviderDescriptorEntity> findByProviderCode(String providerCode);
}

