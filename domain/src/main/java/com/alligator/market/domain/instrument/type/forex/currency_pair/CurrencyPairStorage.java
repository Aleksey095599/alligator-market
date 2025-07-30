package com.alligator.market.domain.instrument.type.forex.currency_pair;

import java.util.List;
import java.util.Optional;

/**
 * Порт хранилища валютных пар.
 */
public interface CurrencyPairStorage {

    /** Сохранить или обновить валютную пару. */
    String save(CurrencyPair pair);

    /** Удалить валютную пару по её коду. */
    void deleteByPairCode(String pairCode);

    /** Найти валютную пару по коду. */
    Optional<CurrencyPair> findByPairCode(String pairCode);

    /** Проверить использование валюты в любой паре. */
    boolean existsByCurrency(String code);

    /** Вернуть все валютные пары. */
    List<CurrencyPair> findAll();
}
