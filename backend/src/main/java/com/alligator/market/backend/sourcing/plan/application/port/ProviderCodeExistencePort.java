package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.provider.vo.ProviderCode;

/**
 * Порт проверки существования провайдера рыночных данных по его коду.
 */
public interface ProviderCodeExistencePort {

    /**
     * Проверяет, существует ли провайдер с указанным кодом.
     */
    boolean existsByCode(ProviderCode code);
}
