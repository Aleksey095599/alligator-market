package com.alligator.market.backend.provider.catalog.repository;

import com.alligator.market.backend.provider.catalog.entity.ProviderCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с каталогом провайдеров.
 */
public interface ProviderCatalogRepository extends JpaRepository<ProviderCatalogEntity, Long> {
}
