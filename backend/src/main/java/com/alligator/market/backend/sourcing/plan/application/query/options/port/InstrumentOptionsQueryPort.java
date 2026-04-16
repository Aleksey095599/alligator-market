package com.alligator.market.backend.sourcing.plan.application.query.options.port;

import com.alligator.market.domain.instrument.base.vo.InstrumentCode;

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
