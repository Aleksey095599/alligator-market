package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с валютами.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    /** Найти JPA-сущность валюты по коду. */
    Optional<CurrencyEntity> findByCode(CurrencyCode code);

    /** Найти JPA-сущность валюты по имени. */
    Optional<CurrencyEntity> findByName(String name);

    /** Удалить запись валюты по коду. */
    void deleteByCode(CurrencyCode code);
}
