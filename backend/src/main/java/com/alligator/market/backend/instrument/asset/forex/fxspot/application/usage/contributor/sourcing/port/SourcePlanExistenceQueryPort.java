package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Query-порт existence-проверки source plan по коду инструмента.
 */
public interface SourcePlanExistenceQueryPort {

    /**
     * Возвращает {@code true}, если source plan существует.
     */
    boolean existsByInstrumentCode(InstrumentCode instrumentCode);
}
