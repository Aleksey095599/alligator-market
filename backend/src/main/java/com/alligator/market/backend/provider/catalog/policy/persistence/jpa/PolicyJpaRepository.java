package com.alligator.market.backend.provider.catalog.policy.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA-репозиторий политики провайдеров.
 */
public interface PolicyJpaRepository extends JpaRepository<PolicyEntity, Long> {
}
