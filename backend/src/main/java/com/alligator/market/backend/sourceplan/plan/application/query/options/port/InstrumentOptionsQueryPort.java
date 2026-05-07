package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

/**
 * Порт получения доступных кодов инструментов для UI.
 */
public interface InstrumentOptionsQueryPort {

    /**
     * Возвращает все доступные коды инструментов.
     */
    List<InstrumentCode> findAllInstrumentCodes();
}
