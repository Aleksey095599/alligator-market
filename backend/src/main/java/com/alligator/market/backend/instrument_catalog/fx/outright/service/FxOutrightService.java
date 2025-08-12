package com.alligator.market.backend.instrument_catalog.fx.outright.service;

import com.alligator.market.domain.instrument.type.fx.outright.model.FxOutright;

import java.util.List;

/**
 * Сервис работы с инструментами FX OUTRIGHT.
 */
public interface FxOutrightService {

    /** Создает записи для указанной валютной пары. */
    void createForPair(String pairCode);

    /** Удаляет записи для указанной валютной пары. */
    void deleteForPair(String pairCode);

    /** Вернуть все инструменты. */
    List<FxOutright> findAll();
}
