package com.alligator.market.backend.currency.repository;

import com.alligator.market.backend.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* Репозиторий для работы с таблицей currency. */
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /* Поиск валюты по ISO-коду (для проверки уникальности) */
    Optional<Currency> findByCode(String code);
}