package com.alligator.market.backend.sourcing.plan.application.query.options.port;

import com.alligator.market.backend.sourcing.plan.application.query.options.model.MDCaptureProcessOption;

import java.util.List;

/**
 * Порт получения доступных процессов фиксации для UI.
 */
public interface MDCaptureProcessOptionsQueryPort {

    /**
     * Возвращает все доступные процессы фиксации.
     */
    List<MDCaptureProcessOption> findAllMDCaptureProcesses();
}
