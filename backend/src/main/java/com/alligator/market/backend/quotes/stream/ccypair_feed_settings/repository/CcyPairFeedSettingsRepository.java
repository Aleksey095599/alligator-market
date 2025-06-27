package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с таблицей 'ccypair_feed_settings'.
 */
public interface CcyPairFeedSettingsRepository extends JpaRepository<CcyPairFeedSettingsEntity, Long> {

    Optional<CcyPairFeedSettingsEntity> findByPair_PairAndProvider_Name(String pair, String provider);

    boolean existsByProvider_Name(String provider);

    // Ищем активные настройки для конкретного PULL провайдера (fetchPeriodMs > 0)
    @Query("""
            from CcyPairFeedSettingsEntity s
            where s.enabled          = true
              and upper(s.provider.name) = upper(:provider)
              and s.fetchPeriodMs    > 0
            """)
    List<CcyPairFeedSettingsEntity> findActivePullSettings(@Param("provider") String provider);

}
