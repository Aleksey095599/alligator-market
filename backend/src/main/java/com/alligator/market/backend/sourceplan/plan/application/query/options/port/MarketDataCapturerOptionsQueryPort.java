package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.MarketDataCapturerOption;

import java.util.List;

/**
 * Порт получения доступных процессов захвата для UI.
 */
public interface MarketDataCapturerOptionsQueryPort {

    /**
     * Возвращает все доступные процессы захвата.
     */
    List<MarketDataCapturerOption> findAllMarketDataCapturers();
}
