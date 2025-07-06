package com.alligator.market.backend.instrument.forex.currency_pair.service;

import com.alligator.market.backend.instrument.forex.currency.entity.CurrencyEntity;
import com.alligator.market.backend.instrument.forex.currency.repository.CurrencyRepository;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.DuplicatePairException;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.PairCurrencyNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.PairNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.repository.PairRepository;
import com.alligator.market.domain.instrument.forex.currency_pair.PairService;
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
public class PairServiceImpl implements PairService {

    private final PairRepository repository;
    private final CurrencyRepository currencyRepository;

    //===================
    // Создать новую пару
    //===================
    @Override
    public String createPair(com.alligator.market.domain.instrument.forex.currency_pair.Pair pairModel) {

        String pair = pairModel.code1() + pairModel.code2();

        repository.findByPair(pair).ifPresent(p -> {
            throw new DuplicatePairException(pair);
        });

        CurrencyEntity c1 = currencyRepository.findByCode(pairModel.code1())
                .orElseThrow(() -> new PairCurrencyNotFoundException(pairModel.code1()));
        CurrencyEntity c2 = currencyRepository.findByCode(pairModel.code2())
                .orElseThrow(() -> new PairCurrencyNotFoundException(pairModel.code2()));

        com.alligator.market.backend.instrument.forex.currency_pair.entity.Pair entity = new com.alligator.market.backend.instrument.forex.currency_pair.entity.Pair();
        entity.setCode1(c1);
        entity.setCode2(c2);
        entity.setPair(pair);
        entity.setDecimal(pairModel.decimal());

        com.alligator.market.backend.instrument.forex.currency_pair.entity.Pair saved = repository.save(entity);
        log.info("Pair {} saved with id={}", saved.getPair(), saved.getId());
        return saved.getPair();
    }

    //==============
    // Обновить пару
    //==============
    @Override
    public void updatePair(com.alligator.market.domain.instrument.forex.currency_pair.Pair pairModel) {

        String pair = pairModel.pair();

        com.alligator.market.backend.instrument.forex.currency_pair.entity.Pair entity = repository.findByPair(pair)
                .orElseThrow(() -> new PairNotFoundException(pair));

        entity.setDecimal(pairModel.decimal());

        repository.save(entity);
        log.info("Pair {} updated (id={})", entity.getPair(), entity.getId());
    }

    //=============
    // Удалить пару
    //=============
    @Override
    public void deletePair(String pair) {

        com.alligator.market.backend.instrument.forex.currency_pair.entity.Pair entity = repository.findByPair(pair)
                .orElseThrow(() -> new PairNotFoundException(pair));

        repository.delete(entity);
        log.info("Pair {} deleted (id={})", entity.getPair(), entity.getId());
    }

    //==================
    // Получить все пары
    //==================
    @Override
    @Transactional(readOnly = true)
    public List<com.alligator.market.domain.instrument.forex.currency_pair.Pair> findAll() {

        List<com.alligator.market.domain.instrument.forex.currency_pair.Pair> result = repository.findAll(Sort.by("pair"))
                .stream()
                .map(p -> new com.alligator.market.domain.instrument.forex.currency_pair.Pair(
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
