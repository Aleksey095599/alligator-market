package com.alligator.market.backend.sourcing.plan.application.query.options.port;

import com.alligator.market.backend.sourcing.plan.application.query.options.model.MDCaptureProcessOption;

import java.util.List;

/**
 * Порт получения доступных процессов захвата для UI.
 */
public interface MDCaptureProcessOptionsQueryPort {

    /**
     * Возвращает все доступные процессы захвата.
     */
    List<MDCaptureProcessOption> findAllMDCaptureProcesses();
}
