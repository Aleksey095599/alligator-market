package com.alligator.market.backend.instrument_catalog.fx.spot.service;

import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntityMapper;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption.PairNotFoundException;
import com.alligator.market.domain.instrument.type.fx.spot.catalog.FxSpotStorage;
import com.alligator.market.domain.instrument.type.fx.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.fx.spot.model.ValueDateCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Реализация сервиса инструментов FX SPOT.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FxSpotInstrumentServiceImpl implements FxSpotInstrumentService {

    private final FxSpotStorage storage;
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
    }

    @Override
    public void deleteForPair(String pairCode) {
        storage.delete(pairCode);
    }
}
