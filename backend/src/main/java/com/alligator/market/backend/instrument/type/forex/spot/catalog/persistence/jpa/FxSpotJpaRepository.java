package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с инструментами FX_SPOT.
 */
public interface FxSpotJpaRepository extends JpaRepository<FxSpotEntity, Long> {

    /** Найти инструмент по внутреннему коду. */
    Optional<FxSpotEntity> findByCode(String code);

    /** Проверить, используется ли валюта в парах. */
    boolean existsByBaseCurrency_CodeOrQuoteCurrency_Code(String baseCurrency, String quoteCurrency);
}
