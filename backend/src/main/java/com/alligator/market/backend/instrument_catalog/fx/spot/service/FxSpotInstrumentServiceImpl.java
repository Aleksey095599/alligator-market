package com.alligator.market.backend.instrument_catalog.fx.spot.service;


import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntityMapper;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption.PairNotFoundException;
import com.alligator.market.domain.instrument.type.fx.spot.catalog.FxSpotInstrumentStorage;
import com.alligator.market.domain.instrument.type.fx.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.fx.spot.model.ValueDateCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Реализация сервиса инструментов FX SPOT.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxSpotInstrumentServiceImpl implements FxSpotInstrumentService {

    private final FxSpotInstrumentStorage storage;
    private final CurrencyPairJpaRepository currencyPairRepository;

    @Override
    public void createForPair(String pairCode) {
        CurrencyPairEntity pair = currencyPairRepository.findByPairCode(pairCode)
                .orElseThrow(() -> new PairNotFoundException(pairCode));
        var currencyPair = CurrencyPairEntityMapper.toDomain(pair);
        // Создаём модель FX SPOT для каждого значения кода даты расчётов
        Arrays.stream(ValueDateCode.values())
                .map(code -> new FxSpot(currencyPair, code))
                .forEach(storage::save);
        log.info("FX Spot instruments for pair {} created", pairCode);
    }

    @Override
    public void deleteForPair(String pairCode) {
        storage.delete(pairCode);
        log.info("FX Spot instruments for pair {} deleted", pairCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = storage.findAll();
        log.debug("Found {} FX Spot instruments", result.size());
        return result;
    }
}
