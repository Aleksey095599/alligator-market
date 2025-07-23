package com.alligator.market.domain.instrument.type.forex.currency_pair;

import java.util.List;
import java.util.Optional;

/**
 * Порт хранилища валютных пар.
 */
public interface CurrencyPairRepository {

    /** Сохранить или обновить валютную пару */
    String save(CurrencyPair pair);

    /** Удалить валютную пару по её коду */
    void deleteByPair(String pair);

    /** Найти валютную пару по коду */
    Optional<CurrencyPair> findByPair(String pair);

    /** Проверить использование валюты в любой паре */
    boolean existsByCurrency(String code);

    /** Вернуть все валютные пары */
    List<CurrencyPair> findAll();
}
