package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей 'ccypair_feed_settings'.
 */
public interface FeedSettingsRepository extends JpaRepository<CcyPairFeedSettingsEntity, Long> {

    Optional<CcyPairFeedSettingsEntity> findByPair_PairAndProvider_Name(String pair, String provider);

    boolean existsByProvider_Name(String provider);

}
