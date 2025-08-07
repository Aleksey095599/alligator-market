package com.alligator.market.backend.instrument_catalog.currency_pair.service;

import com.alligator.market.domain.instrument.currency_pair.catalog.CurrencyFromPairNotFoundException;
import com.alligator.market.domain.instrument.currency_pair.catalog.DuplicatePairException;
import com.alligator.market.domain.instrument.currency_pair.catalog.PairNotFoundException;
import com.alligator.market.domain.instrument.currency_pair.catalog.EqualCurrenciesInPairException;
import com.alligator.market.domain.instrument.currency.catalog.CurrencyStorage;
import com.alligator.market.domain.instrument.currency_pair.CurrencyPair;
import com.alligator.market.domain.instrument.currency_pair.catalog.CurrencyPairStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация контракта сервиса операций с валютными парами в хранилище данных {@link CurrencyPairService}.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyPairServiceImpl implements CurrencyPairService {

    private final CurrencyPairStorage repository;
    private final CurrencyStorage currencyStorage;

    @Override
    public String create(CurrencyPair currencyPair) {

        // Проверяем, что базовая и котируемая валюты разные
        if (currencyPair.base().equals(currencyPair.quote())) {
            throw new EqualCurrenciesInPairException(currencyPair.base());
        }

        // Проверяем, что обе валюты есть в хранилище валют
        currencyStorage.findByCode(currencyPair.base())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.base()));
        currencyStorage.findByCode(currencyPair.quote())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.quote()));

        repository.find(currencyPair.base(), currencyPair.quote()).ifPresent(p -> {
            throw new DuplicatePairException(currencyPair.pairCode());
        });

        String pairCode = repository.save(currencyPair);
        log.info("Currency pair {} saved", pairCode);
        return pairCode;
    }

    @Override
    public void update(CurrencyPair currencyPair) {

        // Проверка наличия валютной пары к обновлению
        repository.find(currencyPair.base(), currencyPair.quote())
                .orElseThrow(() -> new PairNotFoundException(currencyPair.pairCode()));

        repository.save(currencyPair);
        log.info("Currency pair {} updated", currencyPair.pairCode());
    }

    @Override
    public void delete(String base, String quote) {

        String pairCode = base + quote;
        repository.find(base, quote)
                .orElseThrow(() -> new PairNotFoundException(pairCode));

        repository.delete(base, quote);
        log.info("Currency pair {} deleted", pairCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyPair> findAll() {

        List<CurrencyPair> result = repository.findAll();

        log.debug("Found {} currency pairs", result.size());
        return result;
    }
}
