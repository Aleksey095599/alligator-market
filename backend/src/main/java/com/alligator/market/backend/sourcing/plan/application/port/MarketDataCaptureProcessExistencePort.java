package com.alligator.market.backend.sourcing.plan.application.port;

import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

/**
 * Порт проверки существования зарегистрированного процесса захвата рыночных данных по его коду.
 */
public interface MarketDataCaptureProcessExistencePort {

    /**
     * Проверяет, есть ли зарегистрированный процесс захвата рыночных данных с указанным кодом.
     */
    boolean existsByCode(MarketDataCaptureProcessCode code);
}
