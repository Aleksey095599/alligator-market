package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

import java.util.List;

/**
 * Application-сервис (use case) для операций с инструментами FX_SPOT.
 */
public interface FxSpotUseCase {

    /** Сохранить новый инструмент. */
    String create(String baseCurrency, String quoteCurrency, ValueDateCode valueDateCode, Integer quoteDecimal);

    /** Обновить точность котировки. */
    void updateQuoteDecimal(String code, int quoteDecimal);

    /** Удалить инструмент по коду. */
    void delete(String code);

    /** Вернуть все инструменты. */
    List<FxSpot> findAll();
}
