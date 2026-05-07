package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.source.vo.ProviderCode;

/**
 * Порт проверки существования зарегистрированного провайдера рыночных данных по его коду.
 */
public interface ProviderExistencePort {

    /**
     * Проверяет, есть ли зарегистрированный провайдер рыночных данных с указанным кодом.
     */
    boolean existsByCode(ProviderCode code);
}
