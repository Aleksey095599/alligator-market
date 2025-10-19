package com.alligator.market.backend.provider.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для управления провайдерами рыночных данных.
 */
public interface ProviderJpaRepository extends JpaRepository<ProviderEntity, Long> {

    /** Найти JPA-сущность провайдера по техническому коду. */
    Optional<ProviderEntity> findByProviderCode(String providerCode);

    /** TODO: дописать JavaDoc и узнать зачем аннотация @Query. */
    @Query("select p.providerCode from ProviderEntity p")
    List<String> findAllCodes();
}
