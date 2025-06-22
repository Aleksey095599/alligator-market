package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей 'ccypair_feed_settings'.
 */
public interface SettingsRepository extends JpaRepository<CcyPairFeedSettingsEntity, Long> {

    Optional<CcyPairFeedSettingsEntity> findByPair_PairAndProviderAndMode(String pair, String provider, String mode);

}
