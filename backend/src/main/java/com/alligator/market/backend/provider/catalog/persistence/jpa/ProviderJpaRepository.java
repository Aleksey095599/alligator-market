package com.alligator.market.backend.provider.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для управления провайдерами рыночных данных.
 */
public interface ProviderJpaRepository extends JpaRepository<ProviderEntity, Long> {

    /** Найти JPA-сущность провайдера по техническому коду. */
    Optional<ProviderEntity> findByProviderCode(String providerCode);
}
