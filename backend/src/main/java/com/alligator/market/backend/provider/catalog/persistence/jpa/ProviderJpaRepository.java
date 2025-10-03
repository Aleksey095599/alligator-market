package com.alligator.market.backend.provider.catalog.persistence.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для управления провайдерами рыночных данных.
 */
public interface ProviderJpaRepository extends JpaRepository<ProviderEntity, Long> {

    /** Найти JPA-сущность провайдера по техническому коду с пессимистической блокировкой записи. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProviderEntity> findByProviderCode(String providerCode);
}
