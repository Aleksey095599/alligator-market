package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Spring Data JPA-репозиторий дескрипторов.
 */
public interface DescriptorJpaRepository extends JpaRepository<DescriptorEntity, Long> {

    /** Удалить дескрипторы по списку кодов провайдеров. */
    void deleteAllByProviderCodeIn(Collection<String> providerCodes);
}

