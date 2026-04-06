package com.alligator.market.backend.sourcing.plan.application.command.create.port;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;

/**
 * Порт проверки существования инструмента по его коду.
 */
public interface InstrumentCodeExistencePort {

    /**
     * Проверяет, существует ли инструмент с указанным кодом.
     */
    boolean existsByCode(InstrumentCode code);
}
