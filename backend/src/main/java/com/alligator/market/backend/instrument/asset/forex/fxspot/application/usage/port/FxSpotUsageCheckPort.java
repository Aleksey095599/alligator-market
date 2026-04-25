package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Порт проверки использования FX_SPOT инструмента.
 */
public interface FxSpotUsageCheckPort {

    /**
     * Возвращает {@code true}, если инструмент используется хотя бы в одном contributor.
     */
    boolean isUsed(InstrumentCode instrumentCode);
}
