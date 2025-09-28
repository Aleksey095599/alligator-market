package com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Spring Data JPA-репозиторий настроек провайдеров.
 */
public interface ProviderSettingsJpaRepository extends JpaRepository<ProviderSettingsEntity, Long> {

    /** Удалить настройки по списку кодов провайдеров. */
    void deleteAllByProviderCodeIn(Collection<String> providerCodes);
}
