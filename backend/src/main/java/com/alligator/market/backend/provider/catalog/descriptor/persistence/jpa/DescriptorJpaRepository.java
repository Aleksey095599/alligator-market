package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Spring Data JPA-репозиторий дескрипторов.
 */
public interface DescriptorJpaRepository extends JpaRepository<DescriptorEntity, Long> {

    /** Найти JPA-сущность дескриптора по коду провайдера. */
    Optional<DescriptorEntity> findByProviderCode(String providerCode);

    /** Удалить все сущности с кодами из коллекции. */
    void deleteAllByProviderCodeIn(Collection<String> providerCodes);
}
