package com.alligator.market.backend.sourceplan.plan.application.query.existence.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Порт проверки существования SourcePlan по коду инструмента.
 */
public interface SourcePlanExistenceQueryPort {

    /**
     * Возвращает {@code true}, если SourcePlan существует.
     */
    boolean existsByInstrumentCode(InstrumentCode instrumentCode);
}
