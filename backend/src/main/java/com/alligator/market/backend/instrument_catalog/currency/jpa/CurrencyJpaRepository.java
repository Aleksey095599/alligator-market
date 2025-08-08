package com.alligator.market.backend.instrument_catalog.currency.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий валют.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    /** Найти валюту по ISO-коду. */
    Optional<CurrencyEntity> findByCode(String code);

    /** Найти валюту по имени. */
    Optional<CurrencyEntity> findByName(String name);

    /** Найти валюту по стране обращения. */
    Optional<CurrencyEntity> findByCountry(String country);
}
