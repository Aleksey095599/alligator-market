package com.alligator.market.backend.sourcing.plan.application.query.options.port;

import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.List;

/**
 * Порт получения доступных кодов провайдеров для UI.
 */
public interface ProviderOptionsQueryPort {

    /**
     * Возвращает все доступные коды провайдеров.
     */
    List<ProviderCode> findAllProviderCodes();
}
