package com.alligator.market.domain.instrument.type.forex.currency_pair;

import java.util.List;

/**
 * Интерфейс сервиса для операций с валютными парами.
 */
public interface CurrencyPairService {

    String createPair(CurrencyPair pair);

    void updatePair(CurrencyPair pair);

    void deletePair(String pair);

    List<CurrencyPair> findAll();
}