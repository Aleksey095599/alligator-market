package com.alligator.market.backend.instrument_catalog.fx.spot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-репозиторий инструментов FX SPOT.
 */
public interface FxSpotInstrumentJpaRepository extends JpaRepository<FxSpotInstrumentEntity, String> {

    /** Удаляет все записи по коду валютной пары. */
    void deleteAllByCurrencyPair_PairCode(String pairCode);
}
