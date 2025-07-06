package com.alligator.market.backend.instrument.forex.currency_pair.service;

import com.alligator.market.backend.instrument.forex.currency.entity.CurrencyEntity;
import com.alligator.market.backend.instrument.forex.currency.repository.CurrencyRepository;
import com.alligator.market.backend.instrument.forex.currency_pair.dto.PairCreateDto;
import com.alligator.market.backend.instrument.forex.currency_pair.dto.PairDto;
import com.alligator.market.backend.instrument.forex.currency_pair.dto.PairUpdateDto;
import com.alligator.market.backend.instrument.forex.currency_pair.entity.PairEntity;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.DuplicatePairException;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.PairCurrencyNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.PairNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.repository.PairRepository;
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
    public String createPair(PairCreateDto dto) {

        String pair = dto.code1() + dto.code2();

        repository.findByPair(pair).ifPresent(p -> {
            throw new DuplicatePairException(pair);
        });

        CurrencyEntity c1 = currencyRepository.findByCode(dto.code1())
                .orElseThrow(() -> new PairCurrencyNotFoundException(dto.code1()));
        CurrencyEntity c2 = currencyRepository.findByCode(dto.code2())
                .orElseThrow(() -> new PairCurrencyNotFoundException(dto.code2()));

        PairEntity entity = new PairEntity();
        entity.setCode1(c1);
        entity.setCode2(c2);
        entity.setPair(pair);
        entity.setDecimal(dto.decimal());

        PairEntity saved = repository.save(entity);
        log.info("PairEntity {} saved with id={}", saved.getPair(), saved.getId());
        return saved.getPair();
    }

    //==============
    // Обновить пару
    //==============
    @Override
    public void updatePair(String pair, PairUpdateDto dto) {

        PairEntity entity = repository.findByPair(pair)
                .orElseThrow(() -> new PairNotFoundException(pair));

        entity.setDecimal(dto.decimal());

        repository.save(entity);
        log.info("PairEntity {} updated (id={})", entity.getPair(), entity.getId());
    }

    //=============
    // Удалить пару
    //=============
    @Override
    public void deletePair(String pair) {

        PairEntity entity = repository.findByPair(pair)
                .orElseThrow(() -> new PairNotFoundException(pair));

        repository.delete(entity);
        log.info("PairEntity {} deleted (id={})", entity.getPair(), entity.getId());
    }

    //==================
    // Получить все пары
    //==================
    @Override
    @Transactional(readOnly = true)
    public List<PairDto> findAll() {

        List<PairDto> result = repository.findAll(Sort.by("pair"))
                .stream()
                .map(p -> new PairDto(
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
