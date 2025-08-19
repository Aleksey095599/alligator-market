package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с валютами.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    /** Найти валюту по ISO-коду. */
    Optional<CurrencyEntity> findByCode(String code);

    /** Найти валюту по имени. */
    Optional<CurrencyEntity> findByName(String name);

    /** Удалить валюту по ISO-коду. */
    void deleteByCode(String code);
}
