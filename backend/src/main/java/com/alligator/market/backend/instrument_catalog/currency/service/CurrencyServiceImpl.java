package com.alligator.market.backend.instrument_catalog.currency.service;

import com.alligator.market.domain.instrument.currency.catalog.exeption.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.currency.catalog.exeption.CurrencyUsedInPairsException;
import com.alligator.market.domain.instrument.currency.catalog.exeption.DuplicateCurrencyException;
import com.alligator.market.domain.instrument.currency.model.Currency;
import com.alligator.market.domain.instrument.currency.catalog.CurrencyStorage;
import com.alligator.market.domain.instrument.currency_pair.catalog.CurrencyPairStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация контракта {@link CurrencyService}.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyStorage storage;
    private final CurrencyPairStorage pairStorage;

    @Override
    public String createCurrency(Currency currency) {

        storage.findByCode(currency.code()).ifPresent(c -> {
            throw new DuplicateCurrencyException("code", currency.code());
        });
        storage.findByName(currency.name()).ifPresent(c -> {
            throw new DuplicateCurrencyException("name", currency.name());
        });
        storage.findByCountry(currency.country()).ifPresent(c -> {
            throw new DuplicateCurrencyException("country", currency.country());
        });

        String code = storage.save(currency);
        log.info("Currency {} saved", code);
        return code;
    }

    @Override
    public void updateCurrency(Currency currency) {

        // Проверка наличия валюты к обновлению
        storage.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        // Проверки, что обновление не приведет к дублированию
        storage.findByName(currency.name()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new DuplicateCurrencyException("name", currency.name());
            }
        });
        storage.findByCountry(currency.country()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new DuplicateCurrencyException("country", currency.country());
            }
        });

        storage.save(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {

        storage.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        // Проверка, что валюта не используется в парах
        if (pairStorage.existsByCurrency(code)) {
            throw new CurrencyUsedInPairsException(code);
        }

        storage.deleteByCode(code);
        log.info("Currency {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {

        List<Currency> result = storage.findAll();

        log.debug("Found {} currencies", result.size());
        return result;
    }
}
