package com.alligator.market.backend.sourcing.plan.application.query.existence.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Порт проверки существования source plan по коду инструмента.
 */
public interface SourcePlanExistenceQueryPort {

    /**
     * Возвращает {@code true}, если source plan существует.
     */
    boolean existsByInstrumentCode(InstrumentCode instrumentCode);
}
