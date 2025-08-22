package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.model.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Spring Data JPA-репозиторий для работы с профилями провайдеров.
 */
public interface ProviderProfileJpaRepository extends JpaRepository<ProviderProfileEntity, Long> {

    /** Найти все профили по заданному статусу. */
    List<ProviderProfileEntity> findAllByStatus(ProviderStatus status);
}
