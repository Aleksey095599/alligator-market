package com.alligator.market.backend.currencies.repository;

import com.alligator.market.backend.currencies.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей currency.
 */
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCode(String code);
    Optional<Currency> findByName(String name);
    Optional<Currency> findByCountry(String country);

}