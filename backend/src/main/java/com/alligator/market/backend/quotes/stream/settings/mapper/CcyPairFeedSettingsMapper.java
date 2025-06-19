package com.alligator.market.backend.quotes.stream.settings.mapper;

import com.alligator.market.backend.pairs.entity.Pair;
import com.alligator.market.backend.quotes.stream.settings.entity.CcyPairFeedSettingsEntity;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменной моделью и соответствующим Entity.
 * Импорт доменной модели в backend обусловлен использованием этой модели в нескольких слоях.
 */
@Component
public class CcyPairFeedSettingsMapper {

    public com.alligator.market.domain.model.CcyPairFeedSettings toDomain(CcyPairFeedSettingsEntity entity) {
        return new com.alligator.market.domain.model.CcyPairFeedSettings(
                entity.getPair().getPair(),
                entity.getProvider(),
                entity.getPriority(),
                entity.getRefreshMs(),
                entity.isEnabled()
        );
    }

    public CcyPairFeedSettingsEntity toEntity(com.alligator.market.domain.model.CcyPairFeedSettings cfg, Pair pair) {
        CcyPairFeedSettingsEntity entity = new CcyPairFeedSettingsEntity();
        entity.setPair(pair);
        entity.setProvider(cfg.provider());
        entity.setPriority(cfg.priority());
        entity.setRefreshMs(cfg.refreshMs());
        entity.setEnabled(cfg.enabled());
        return entity;
    }
}
