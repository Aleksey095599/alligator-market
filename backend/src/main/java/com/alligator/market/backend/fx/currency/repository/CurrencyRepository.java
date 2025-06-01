package com.alligator.market.backend.fx.currency.repository;

import com.alligator.market.backend.fx.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* Репозиторий для работы с таблицей currency. */
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCode(String code);
    Optional<Currency> findByName(String name);
    Optional<Currency> findByCountry(String country);
}