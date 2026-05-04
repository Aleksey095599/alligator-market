package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

/**
 * Порт проверки существования процесса фиксации по его коду.
 */
public interface CaptureProcessCodeExistencePort {

    /**
     * Проверяет, существует ли процесс фиксации с указанным кодом.
     */
    boolean existsByCode(CaptureProcessCode code);
}
