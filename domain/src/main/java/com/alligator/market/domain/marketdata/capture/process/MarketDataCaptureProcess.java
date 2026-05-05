package com.alligator.market.domain.marketdata.capture.process;

import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.MarketDataCaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

/**
 * Процесс захвата рыночных данных.
 */
public interface MarketDataCaptureProcess {

    /**
     * Уникальный код процесса.
     */
    MarketDataCaptureProcessCode processCode();

    /**
     * Паспорт процесса.
     */
    MarketDataCaptureProcessPassport passport();

    /**
     * Политика процесса.
     */
    MarketDataCaptureProcessPolicy policy();
}
