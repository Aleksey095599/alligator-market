package com.alligator.market.backend.instrument_catalog.fx.outright.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA-репозиторий инструментов FX OUTRIGHT.
 */
public interface FxOutrightJpaRepository extends JpaRepository<FxOutrightEntity, String> {

    /** Удаляет все записи по коду валютной пары. */
    void deleteAllByCurrencyPair_PairCode(String pairCode);
}
