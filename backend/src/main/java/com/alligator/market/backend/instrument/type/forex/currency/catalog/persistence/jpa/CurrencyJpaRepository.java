package com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.type.forex.currency.vo.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с валютами.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    /**
     * Найти JPA-сущность валюты по коду.
     */
    Optional<CurrencyEntity> findByCode(CurrencyCode code);
}
