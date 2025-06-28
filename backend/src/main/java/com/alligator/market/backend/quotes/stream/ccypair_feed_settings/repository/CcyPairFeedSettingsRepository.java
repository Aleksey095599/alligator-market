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

    // Извлекаем в виде списка настройки потока валютных пар для заданного провайдера, только активные (enabled),
    // только PULL (fetchPeriodMs > 0)
    @Query("""
            select s
            from CcyPairFeedSettingsEntity s
            where s.provider.name = :provider
              and s.enabled = true
              and s.fetchPeriodMs > 0
            order by s.id
            """)
    List<CcyPairFeedSettingsEntity> findActivePullSettings(@Param("provider") String provider);

}
