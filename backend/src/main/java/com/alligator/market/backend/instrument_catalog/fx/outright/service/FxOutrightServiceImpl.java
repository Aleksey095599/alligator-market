package com.alligator.market.backend.instrument_catalog.fx.outright.service;


import com.alligator.market.backend.instrument.type.fx.outright.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument.type.fx.outright.reference.currency_pair.jpa.CurrencyPairEntityMapper;
import com.alligator.market.backend.instrument.type.fx.outright.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.domain.instrument.type.fx.outright.reference.currency_pair.catalog.exeption.PairNotFoundException;
import com.alligator.market.domain.instrument.type.fx.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.fx.outright.model.FxOutright;
import com.alligator.market.domain.instrument.type.fx.outright.model.ValueDateCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Реализация сервиса инструментов FX OUTRIGHT.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxOutrightServiceImpl implements FxOutrightService {

    private final FxOutrightStorage storage;
    private final CurrencyPairJpaRepository currencyPairRepository;

    @Override
    public void createForPair(String pairCode) {
        CurrencyPairEntity pair = currencyPairRepository.findByPairCode(pairCode)
                .orElseThrow(() -> new PairNotFoundException(pairCode));
        var currencyPair = CurrencyPairEntityMapper.toDomain(pair);
        // Создаём модель FX OUTRIGHT для каждого значения кода даты расчётов
        Arrays.stream(ValueDateCode.values())
                .map(code -> new FxOutright(currencyPair, code))
                .forEach(storage::save);
        log.info("FX OUTRIGHT instruments for pair {} created", pairCode);
    }

    @Override
    public void deleteForPair(String pairCode) {
        storage.delete(pairCode);
        log.info("FX OUTRIGHT instruments for pair {} deleted", pairCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxOutright> findAll() {
        List<FxOutright> result = storage.findAll();
        log.debug("Found {} FX OUTRIGHT instruments", result.size());
        return result;
    }
}
