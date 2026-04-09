package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

/**
 * Порт проверки существования провайдера по его коду.
 */
public interface ProviderCodeExistencePort {

    /**
     * Проверяет, существует ли провайдер с указанным кодом.
     */
    boolean existsByCode(ProviderCode code);
}
