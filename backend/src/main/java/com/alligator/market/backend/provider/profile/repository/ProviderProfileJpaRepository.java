package com.alligator.market.backend.provider.profile.repository;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с профилями провайдеров.
 */
public interface ProviderProfileJpaRepository extends JpaRepository<ProviderProfileEntity, Long> {
}
