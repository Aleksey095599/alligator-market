package com.alligator.market.domain.marketdata.capture.process;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.CaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;
import java.util.Set;

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

    /**
     * Коды инструментов, поддерживаемых процессом.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Проверяет, поддерживается ли инструмент по коду.
     */
    default boolean supportsInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return supportedInstrumentCodes().contains(instrumentCode);
    }
}
