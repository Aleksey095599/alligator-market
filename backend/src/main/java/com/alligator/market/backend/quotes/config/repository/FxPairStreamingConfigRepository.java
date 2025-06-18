package com.alligator.market.backend.quotes.config.repository;

import com.alligator.market.backend.quotes.config.entity.FxPairStreamingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий таблицы конфигураций стрима котировок для заданной валютной пары.
 */
public interface FxPairStreamingConfigRepository extends JpaRepository<FxPairStreamingConfig, Long> {

    Optional<FxPairStreamingConfig> findByPair_PairAndProvider(String pair, String provider);
}
