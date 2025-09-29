package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA-репозиторий дескрипторов.
 */
public interface DescriptorJpaRepository extends JpaRepository<DescriptorEntity, Long> {

}
