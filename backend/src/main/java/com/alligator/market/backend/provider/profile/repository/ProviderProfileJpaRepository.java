package com.alligator.market.backend.provider.profile.repository;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Репозиторий для работы с профилями провайдеров.
 */
public interface ProviderProfileJpaRepository extends JpaRepository<ProviderProfileEntity, Long> {

    /** Найти все профили по статусу */
    List<ProviderProfileEntity> findAllByStatus(ProviderProfileStatus status);
}
