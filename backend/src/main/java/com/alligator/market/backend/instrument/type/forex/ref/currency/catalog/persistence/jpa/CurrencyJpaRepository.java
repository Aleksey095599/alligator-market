package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с валютами.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    /** Найти сущность валюты по ISO-коду. */
    Optional<CurrencyEntity> findByCode(String code);

    /** Найти сущность валюты по имени. */
    Optional<CurrencyEntity> findByName(String name);

    /** Удалить запись валюты по ISO-коду. */
    void deleteByCode(String code);
}
