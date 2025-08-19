package com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с инструментами FX_OUTRIGHT.
 */
public interface FxOutrightJpaRepository extends JpaRepository<FxOutrightEntity, Long> {

    /** Найти инструмент по внутреннему коду. */
    Optional<FxOutrightEntity> findByCode(String code);

    /** Проверить, используется ли валюта в парах. */
    boolean existsByBaseCurrency_CodeOrQuoteCurrency_Code(String baseCurrency, String quoteCurrency);
}
