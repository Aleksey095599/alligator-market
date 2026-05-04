package com.alligator.market.domain.marketdata.capture.process;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.policy.CaptureProcessPolicy;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;
import java.util.Set;

/**
 * Доменное описание процесса фиксации рыночных данных.
 */
public interface MarketDataCaptureProcess {

    /**
     * Уникальный код процесса фиксации рыночных данных.
     */
    CaptureProcessCode processCode();

    /**
     * Паспорт процесса фиксации рыночных данных.
     */
    CaptureProcessPassport passport();

    /**
     * Политика процесса фиксации рыночных данных.
     */
    CaptureProcessPolicy policy();

    /**
     * Внутренние инструменты приложения, которые может обслуживать этот процесс.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Проверяет, поддерживается ли инструмент этим процессом фиксации.
     */
    default boolean supportsInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return supportedInstrumentCodes().contains(instrumentCode);
    }
}
