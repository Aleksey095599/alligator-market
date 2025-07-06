package com.alligator.market.domain.instrument.forex.currency_pair;

import java.util.List;

/**
 * Интерфейс сервиса для операций с валютными парами.
 */
public interface PairService {

    String createPair(Pair pair);

    void updatePair(Pair pair);

    void deletePair(String pair);

    List<Pair> findAll();
}
