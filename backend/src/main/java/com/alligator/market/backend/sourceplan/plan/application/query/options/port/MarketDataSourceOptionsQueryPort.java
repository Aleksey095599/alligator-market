package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.List;

/**
 * Порт получения доступных кодов провайдеров для UI.
 */
public interface MarketDataSourceOptionsQueryPort {

    /**
     * Возвращает все доступные коды провайдеров.
     */
    List<MarketDataSourceCode> findAllSourceCodes();
}
