package com.alligator.market.domain.marketdata.capture.process;

import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.MDCaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

/**
 * Процесс захвата рыночных данных.
 */
public interface MDCaptureProcess {

    /**
     * Уникальный код процесса.
     */
    MDCaptureProcessCode processCode();

    /**
     * Паспорт процесса.
     */
    MDCaptureProcessPassport passport();

    /**
     * Политика процесса.
     */
    MDCaptureProcessPolicy policy();
}
