package com.alligator.market.backend.instrument.type.forex.currency_pair.service;

import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.CurrencyFromPairNotFoundException;
import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.DuplicatePairException;
import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.PairNotFoundException;
import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.EqualCurrenciesInPairException;
import com.alligator.market.domain.instrument.type.forex.currency.CurrencyStorage;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPairStorage;
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

    //===================
    // Создать новую пару
    //===================
    @Override
    public String createPair(CurrencyPair currencyPair) {

        if (currencyPair.base().equals(currencyPair.quote())) {
            throw new EqualCurrenciesInPairException(currencyPair.base());
        }

        currencyStorage.findByCode(currencyPair.base())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.base()));
        currencyStorage.findByCode(currencyPair.quote())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.quote()));

        repository.findByPairCode(currencyPair.pairCode()).ifPresent(p -> {
            throw new DuplicatePairException(currencyPair.pairCode());
        });

        String pairCode = repository.save(currencyPair);
        log.info("Currency pair {} saved", pairCode);
        return pairCode;
    }

    //==============
    // Обновить пару
    //==============
    @Override
    public void updatePair(CurrencyPair currencyPair) {

        // Проверка наличия валютной пары к обновлению
        repository.findByPairCode(currencyPair.pairCode())
                .orElseThrow(() -> new PairNotFoundException(currencyPair.pairCode()));

        repository.save(currencyPair);
        log.info("Currency pair {} updated", currencyPair.pairCode());
    }

    //=============
    // Удалить пару
    //=============
    @Override
    public void deletePair(String pairCode) {

        repository.findByPairCode(pairCode)
                .orElseThrow(() -> new PairNotFoundException(pairCode));

        repository.deleteByPairCode(pairCode);
        log.info("Currency pair {} deleted", pairCode);
    }

    //==================
    // Получить все пары
    //==================
    @Override
    @Transactional(readOnly = true)
    public List<CurrencyPair> findAll() {

        // Извлекаем все валютные пары, преобразуя список сущностей к доменной модели валютной пары
        List<CurrencyPair> result = repository.findAll();

        log.debug("Found {} currency pairs", result.size());
        return result;
    }
}
