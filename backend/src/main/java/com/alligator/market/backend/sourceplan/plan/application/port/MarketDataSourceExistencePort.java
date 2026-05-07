package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

/**
 * Порт проверки существования зарегистрированного провайдера рыночных данных по его коду.
 */
public interface MarketDataSourceExistencePort {

    /**
     * Проверяет, есть ли зарегистрированный провайдер рыночных данных с указанным кодом.
     */
    boolean existsByCode(MarketDataSourceCode code);
}
