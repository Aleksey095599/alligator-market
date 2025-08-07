package com.alligator.market.backend.instrument_catalog.currency_pair.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий для операций с сущностями {@link CurrencyPairEntity}.
 */
public interface CurrencyPairJpaRepository extends JpaRepository<CurrencyPairEntity, Long> {


    Optional<CurrencyPairEntity> findByPairCode(String pairCode);

    /**
     * Проверяет, существует ли хотя бы одна валютная пара,
     * которая использует валюту с кодом base или quote.
     */
    boolean existsByBase_CodeOrQuote_Code(String base, String quote);
}

