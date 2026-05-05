package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

/**
 * Порт проверки существования процесса захвата по его коду.
 */
public interface MDCaptureProcessCodeExistencePort {

    /**
     * Проверяет, существует ли процесс захвата с указанным кодом.
     */
    boolean existsByCode(MDCaptureProcessCode code);
}
