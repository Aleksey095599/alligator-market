package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * JPA-репозиторий для профилей провайдеров рыночных данных.
 */
public interface ProviderProfileJpaRepository extends JpaRepository<ProviderProfileEntity, Long> {

    /** Найти все профили по заданному статусу. */
    List<ProviderProfileEntity> findAllByStatus(ProviderProfileStatus status);
}
