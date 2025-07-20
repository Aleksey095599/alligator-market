package com.alligator.market.backend.instrument.type.forex.currency_pair.service;

import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.CurrencyFromPairNotFoundException;
import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.DuplicatePairException;
import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.PairNotFoundException;
import com.alligator.market.backend.instrument.type.forex.currency_pair.exception.EqualCurrenciesInPairException;
import com.alligator.market.domain.instrument.type.forex.currency.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;
import com.alligator.market.backend.instrument.type.forex.currency_pair.service.CurrencyPairService;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация интерфейса сервиса для валютных пар.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyPairServiceImpl implements CurrencyPairService {

    private final CurrencyPairRepository repository;
    private final CurrencyRepository currencyRepository;

    //===================
    // Создать новую пару
    //===================
    @Override
    public String createPair(CurrencyPair currencyPair) {

        if (currencyPair.code1().equals(currencyPair.code2())) {
            throw new EqualCurrenciesInPairException(currencyPair.code1());
        }

        currencyRepository.findByCode(currencyPair.code1())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.code1()));
        currencyRepository.findByCode(currencyPair.code2())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.code2()));

        repository.findByPair(currencyPair.pair()).ifPresent(p -> {
            throw new DuplicatePairException(currencyPair.pair());
        });

        String pair = repository.save(currencyPair);
        log.info("Currency pair {} saved", pair);
        return pair;
    }

    //==============
    // Обновить пару
    //==============
    @Override
    public void updatePair(CurrencyPair currencyPair) {

        // Проверка наличия валютной пары к обновлению
        repository.findByPair(currencyPair.pair())
                .orElseThrow(() -> new PairNotFoundException(currencyPair.pair()));

        repository.save(currencyPair);
        log.info("Currency pair {} updated", currencyPair.pair());
    }

    //=============
    // Удалить пару
    //=============
    @Override
    public void deletePair(String currencyPair) {

        repository.findByPair(currencyPair)
                .orElseThrow(() -> new PairNotFoundException(currencyPair));

        repository.deleteByPair(currencyPair);
        log.info("Currency pair {} deleted", currencyPair);
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
