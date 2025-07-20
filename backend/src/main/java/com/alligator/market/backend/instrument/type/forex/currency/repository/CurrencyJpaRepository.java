package com.alligator.market.backend.instrument.type.forex.currency.repository;

import com.alligator.market.backend.instrument.type.forex.currency.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей currency.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> findByCode(String code);
    Optional<CurrencyEntity> findByName(String name);
    Optional<CurrencyEntity> findByCountry(String country);

}