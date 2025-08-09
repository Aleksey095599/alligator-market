package com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.service;

import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.model.CurrencyPair;

import java.util.List;

/**
 * Сервис работы с валютными парами.
 */
public interface CurrencyPairService {

    /** Создать валютную пару. */
    String create(CurrencyPair pair);

    /** Обновить валютную пару. */
    void update(CurrencyPair pair);

    /** Удалить пару по коду базовой и котируемой валют. */
    void delete(String base, String quote);

    /** Вернуть все валютные пары. */
    List<CurrencyPair> findAll();
}
