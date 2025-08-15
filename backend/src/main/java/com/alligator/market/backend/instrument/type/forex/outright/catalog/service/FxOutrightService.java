package com.alligator.market.backend.instrument.type.forex.outright.catalog.service;

import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;

import java.util.List;

/**
 * Сервис работы с инструментами FX_OUTRIGHT.
 */
public interface FxOutrightService {

    /** Сохранить новый инструмент. */
    String create(FxOutright instrument);

    /** Удалить инструмент по коду. */
    void delete(String code);

    /** Вернуть все инструменты. */
    List<FxOutright> findAll();
}
