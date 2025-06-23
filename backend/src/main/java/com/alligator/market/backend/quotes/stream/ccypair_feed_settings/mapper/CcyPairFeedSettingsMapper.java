package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.mapper;

import com.alligator.market.backend.ccypairs.entity.Pair;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.stream.providers.list.entity.Provider;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменной моделью и соответствующим Entity.
 * Импорт доменной модели в backend обусловлен использованием этой модели в других слоях.
 */
@Component
public class CcyPairFeedSettingsMapper {

    public com.alligator.market.domain.quotes.stream.settings.CcyPairFeedSettings toDomain(CcyPairFeedSettingsEntity entity) {
        return new com.alligator.market.domain.quotes.stream.settings.CcyPairFeedSettings(
                entity.getPair().getPair(),
                entity.getProvider().getName(),
                entity.getPriority(),
                entity.getFetchPeriodMs(),
                entity.isEnabled()
        );
    }

    public CcyPairFeedSettingsEntity toEntity(
            com.alligator.market.domain.quotes.stream.settings.CcyPairFeedSettings cfg,
            Pair pair,
            Provider provider) {
        CcyPairFeedSettingsEntity entity = new CcyPairFeedSettingsEntity();
        entity.setPair(pair);
        entity.setProvider(provider);
        entity.setPriority(cfg.priority());
        entity.setFetchPeriodMs(cfg.fetchPeriodMs());
        entity.setEnabled(cfg.enabled());
        return entity;
    }
}
