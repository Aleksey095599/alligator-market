package com.alligator.market.domain.instrument.type.fx.outright.reference.currency_pair.catalog;

import com.alligator.market.domain.instrument.type.fx.outright.reference.currency_pair.model.CurrencyPair;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище валютных пар.
 */
public interface CurrencyPairStorage {

    /** Сохранить валютную пару. */
    String save(CurrencyPair pair);

    /** Удалить пару по коду базовой и котируемой валют. */
    void delete(String base, String quote);

    /** Найти пару по коду базовой и котируемой валют. */
    Optional<CurrencyPair> find(String base, String quote);

    /** Проверить, используется ли валюта в каких-либо парах. */
    boolean existsByCurrency(String currencyCode);

    /** Вернуть все валютные пары. */
    List<CurrencyPair> findAll();
}
