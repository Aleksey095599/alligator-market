package com.alligator.market.backend.marketdata.capture.application.port;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Application-порт разрешения доменного инструмента для фиксации рыночных данных.
 */
public interface MarketDataCaptureInstrumentResolver {

    /**
     * Возвращает доменный инструмент по коду или сигнализирует, что он недоступен для capture-сценария.
     */
    Instrument resolveRequired(InstrumentCode instrumentCode);
}
