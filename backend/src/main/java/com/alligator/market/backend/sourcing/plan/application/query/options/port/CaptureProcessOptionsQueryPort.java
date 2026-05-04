package com.alligator.market.backend.sourcing.plan.application.query.options.port;

import com.alligator.market.backend.sourcing.plan.application.query.options.model.CaptureProcessOption;

import java.util.List;

/**
 * Порт получения доступных процессов фиксации для UI.
 */
public interface CaptureProcessOptionsQueryPort {

    /**
     * Возвращает все доступные процессы фиксации.
     */
    List<CaptureProcessOption> findAllCaptureProcesses();
}
