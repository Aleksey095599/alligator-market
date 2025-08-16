package com.alligator.market.backend.provider.catalog.jpa;

import com.alligator.market.domain.provider.model.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * JPA-репозиторий профилей провайдеров рыночных данных (далее - профили).
 */
public interface ProviderProfileJpaRepository extends JpaRepository<ProviderEntity, Long> {

    /** Найти все профили по заданному статусу. */
    List<ProviderEntity> findAllByStatus(ProviderStatus status);
}
