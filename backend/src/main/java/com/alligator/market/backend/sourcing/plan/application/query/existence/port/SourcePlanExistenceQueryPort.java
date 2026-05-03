package com.alligator.market.backend.sourcing.plan.application.query.existence.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Порт проверки существования MarketDataSourcePlan по коду инструмента.
 */
public interface SourcePlanExistenceQueryPort {

    /**
     * Возвращает {@code true}, если MarketDataSourcePlan существует.
     */
    boolean existsByInstrumentCode(InstrumentCode instrumentCode);
}
