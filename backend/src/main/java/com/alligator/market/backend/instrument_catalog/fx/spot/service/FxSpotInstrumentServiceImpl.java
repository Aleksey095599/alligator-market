package com.alligator.market.backend.instrument_catalog.fx.spot.service;

import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.backend.instrument_catalog.fx.spot.jpa.FxSpotInstrumentEntity;
import com.alligator.market.backend.instrument_catalog.fx.spot.jpa.FxSpotInstrumentJpaRepository;
import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption.PairNotFoundException;
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

    private final FxSpotInstrumentJpaRepository repository;
    private final CurrencyPairJpaRepository currencyPairRepository;

    @Override
    public void createForPair(String pairCode) {
        CurrencyPairEntity pair = currencyPairRepository.findByPairCode(pairCode)
                .orElseThrow(() -> new PairNotFoundException(pairCode));
        // Создаем запись для каждого значения кода даты расчетов
        Arrays.stream(ValueDateCode.values()).forEach(code -> {
            FxSpotInstrumentEntity entity = new FxSpotInstrumentEntity();
            entity.setInternalCode(pairCode + "_" + code);
            entity.setCurrencyPair(pair);
            entity.setValueDateCode(code);
            repository.save(entity);
        });
    }

    @Override
    public void deleteForPair(String pairCode) {
        repository.deleteAllByCurrencyPair_PairCode(pairCode);
    }
}
