package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

/**
 * Порт проверки существования зарегистрированного процесса захвата рыночных данных по его коду.
 */
public interface MDCaptureProcessCodeExistencePort {

    /**
     * Проверяет, есть ли зарегистрированный процесс захвата рыночных данных с указанным кодом.
     */
    boolean existsByCode(MDCaptureProcessCode code);
}
