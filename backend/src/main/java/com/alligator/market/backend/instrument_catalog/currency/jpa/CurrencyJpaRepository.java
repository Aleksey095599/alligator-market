package com.alligator.market.backend.instrument_catalog.currency.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий для операций с сущностями {@link CurrencyEntity}.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> findByCode(String code);
    Optional<CurrencyEntity> findByName(String name);
    Optional<CurrencyEntity> findByCountry(String country);
}
