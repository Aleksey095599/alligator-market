package com.alligator.market.backend.sourcing.plan.application.query.options.port;

import com.alligator.market.backend.sourcing.plan.application.query.options.model.MarketDataCaptureProcessOption;

import java.util.List;

/**
 * Порт получения доступных процессов захвата для UI.
 */
public interface MarketDataCaptureProcessOptionsQueryPort {

    /**
     * Возвращает все доступные процессы захвата.
     */
    List<MarketDataCaptureProcessOption> findAllMarketDataCaptureProcesses();
}
