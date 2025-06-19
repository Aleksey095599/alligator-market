package com.alligator.market.backend.quotes.config.mapper;

import com.alligator.market.backend.pairs.entity.Pair;
import com.alligator.market.backend.quotes.config.entity.FxPairStreamingConfig;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменной моделью и соответствующим entity.
 * Импорт доменной модели в backend обусловлен использованием этой модели в нескольких слоях.
 */
@Component
public class FxPairStreamingConfigMapper {

    public com.alligator.market.domain.model.CcyPairFeedSettings toDomain(FxPairStreamingConfig entity) {
        return new com.alligator.market.domain.model.CcyPairFeedSettings(
                entity.getPair().getPair(),
                entity.getProvider(),
                entity.getPriority(),
                entity.getRefreshMs(),
                entity.isEnabled()
        );
    }

    public FxPairStreamingConfig toEntity(com.alligator.market.domain.model.CcyPairFeedSettings cfg, Pair pair) {
        FxPairStreamingConfig entity = new FxPairStreamingConfig();
        entity.setPair(pair);
        entity.setProvider(cfg.provider());
        entity.setPriority(cfg.priority());
        entity.setRefreshMs(cfg.refreshMs());
        entity.setEnabled(cfg.enabled());
        return entity;
    }
}
