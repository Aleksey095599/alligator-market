package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

/**
 * Порт проверки существования зарегистрированного процесса захвата рыночных данных по его коду.
 */
public interface MarketDataCapturerExistencePort {

    /**
     * Проверяет, есть ли зарегистрированный процесс захвата рыночных данных с указанным кодом.
     */
    boolean existsByCode(MarketDataCapturerCode code);
}
