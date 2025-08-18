package com.alligator.market.backend.instrument.type.forex.outright.catalog.service;

import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;

import java.util.List;

/**
 * Сервисный контракт, используемый REST-слоем для работы с инструментами FX_OUTRIGHT:
 * создание, обновление, удаление и получение списка и т.п.
 */
public interface FxOutrightService {

    /** Сохранить новый инструмент. */
    String create(FxOutright instrument);

    /** Обновить точность котировки. */
    void updateQuoteDecimal(String code, int quoteDecimal);

    /** Удалить инструмент по коду. */
    void delete(String code);

    /** Вернуть все инструменты. */
    List<FxOutright> findAll();
}
