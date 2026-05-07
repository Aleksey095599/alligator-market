package com.alligator.market.backend.sourceplan.plan.application.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

/**
 * Порт проверки существования зарегистрированного финансового инструмента по его коду.
 */
public interface InstrumentExistencePort {

    /**
     * Проверяет, есть ли зарегистрированный финансовый инструмент с указанным кодом.
     */
    boolean existsByCode(InstrumentCode code);
}
