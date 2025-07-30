package com.alligator.market.backend.instrument.type.forex.currency.repository;

import com.alligator.market.backend.instrument.type.forex.currency.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий для работы с сущностями {@link CurrencyEntity} в таблице <code>currency</code>.
 */
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> findByCode(String code);
    Optional<CurrencyEntity> findByName(String name);
    Optional<CurrencyEntity> findByCountry(String country);
}