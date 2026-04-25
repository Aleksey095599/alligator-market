package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Проверка использования FX_SPOT инструмента конкретным внешним contributor.
 */
public interface FxSpotUsageContributor {

    /**
     * Флаг, сигнализирующий об использовании инструмента конкретным contributor.
     */
    boolean isUsed(InstrumentCode instrumentCode);
}
