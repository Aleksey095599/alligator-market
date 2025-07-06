package com.alligator.market.backend.instrument.forex.currency_pair.service;

import com.alligator.market.backend.instrument.forex.currency.entity.CurrencyEntity;
import com.alligator.market.backend.instrument.forex.currency.repository.CurrencyRepository;
import com.alligator.market.backend.instrument.forex.currency_pair.entity.PairEntity;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.CurrencyFromPairNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.DuplicatePairException;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.PairNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.repository.PairRepository;
import com.alligator.market.domain.instrument.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.instrument.forex.currency_pair.CurrencyPairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
public class PairServiceImpl implements CurrencyPairService {

    private final PairRepository repository;
    private final CurrencyRepository currencyRepository;

    //===================
    // Создать новую пару
    //===================
    @Override
    public String createPair(CurrencyPair currencyPair) {

        repository.findByPair(currencyPair.pair()).ifPresent(p -> {
            throw new DuplicatePairException(currencyPair.pair());
        });

        CurrencyEntity c1 = currencyRepository.findByCode(currencyPair.code1())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.code1()));
        CurrencyEntity c2 = currencyRepository.findByCode(currencyPair.code2())
                .orElseThrow(() -> new CurrencyFromPairNotFoundException(currencyPair.code2()));

        PairEntity entity = new PairEntity();
        entity.setCode1(c1);
        entity.setCode2(c2);
        entity.setPair(currencyPair.pair());
        entity.setDecimal(currencyPair.decimal());

        PairEntity saved = repository.save(entity);
        log.info("Currency pair {} saved with id={}", saved.getPair(), saved.getId());
        return saved.getPair();
    }

    //==============
    // Обновить пару
    //==============
    @Override
    public void updatePair(CurrencyPair currencyPair) {

        // Проверка наличия валютной пары к обновлению
        PairEntity entity = repository.findByPair(currencyPair.pair())
                .orElseThrow(() -> new PairNotFoundException(currencyPair.pair()));

        // Обновляем сущность
        entity.setDecimal(currencyPair.decimal());

        repository.save(entity);
        log.info("Currency pair {} updated (id={})", entity.getPair(), entity.getId());
    }

    //=============
    // Удалить пару
    //=============
    @Override
    public void deletePair(String currencyPair) {

        PairEntity entity = repository.findByPair(currencyPair)
                .orElseThrow(() -> new PairNotFoundException(currencyPair));

        repository.delete(entity);
        log.info("PairEntity {} deleted (id={})", entity.getPair(), entity.getId());
    }

    //==================
    // Получить все пары
    //==================
    @Override
    @Transactional(readOnly = true)
    public List<CurrencyPair> findAll() {

        // Извлекаем все валютные пары, преобразуя список сущностей к доменной модели валютной пары
        List<CurrencyPair> result = repository.findAll(Sort.by("pair"))
                .stream()
                .map(p -> new CurrencyPair(
                        p.getCode1().getCode(),
                        p.getCode2().getCode(),
                        p.getPair(),
                        p.getDecimal()
                ))
                .toList();

        log.debug("Found {} pairs", result.size());
        return result;
    }
}
