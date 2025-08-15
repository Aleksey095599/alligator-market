package com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий инструментов FX_OUTRIGHT.
 */
public interface FxOutrightJpaRepository extends JpaRepository<FxOutrightEntity, Long> {

    /** Найти инструмент по внутреннему коду. */
    Optional<FxOutrightEntity> findByInstrumentCode(String instrumentCode);
}
