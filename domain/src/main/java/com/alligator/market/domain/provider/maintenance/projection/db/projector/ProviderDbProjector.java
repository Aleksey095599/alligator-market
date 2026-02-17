package com.alligator.market.domain.provider.maintenance.projection.db.projector;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Map;

/**
 * Проектор данных о провайдерах рыночных данных в БД.
 */
public interface ProviderDbProjector {

    /**
     * Проекция карты паспортов провайдеров, индексированной по коду провайдера.
     */
    void passportsProjection(Map<ProviderCode, ProviderPassport> passportsByCode);
}
