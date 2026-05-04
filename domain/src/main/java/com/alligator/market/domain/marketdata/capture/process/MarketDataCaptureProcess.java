package com.alligator.market.domain.marketdata.capture.process;

import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.CaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

/**
 * Процесс фиксации рыночных данных.
 */
public interface MarketDataCaptureProcess {

    /**
     * Уникальный код процесса.
     */
    CaptureProcessCode processCode();

    /**
     * Паспорт процесса.
     */
    CaptureProcessPassport passport();

    /**
     * Политика процесса.
     */
    CaptureProcessPolicy policy();
}
