package com.alligator.market.backend.instrument_catalog.fx.spot.adapter;

import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntityMapper;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.backend.instrument_catalog.fx.spot.jpa.FxSpotInstrumentEntity;
import com.alligator.market.backend.instrument_catalog.fx.spot.jpa.FxSpotInstrumentJpaRepository;
import com.alligator.market.domain.instrument.type.fx.spot.catalog.FxSpotInstrumentStorage;
import com.alligator.market.domain.instrument.type.fx.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Адаптер хранилища инструментов FX SPOT на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxSpotInstrumentStorageAdapter implements FxSpotInstrumentStorage {

    private final FxSpotInstrumentJpaRepository jpaRepository;
    private final CurrencyPairJpaRepository currencyPairJpaRepository;

    @Override
    public void save(FxSpot instrument) {
        CurrencyPairEntity pair = currencyPairJpaRepository
                .findByPairCode(instrument.currencyPair().pairCode())
                .orElseThrow();

        FxSpotInstrumentEntity entity = new FxSpotInstrumentEntity();
        entity.setInternalCode(instrument.internalCode());
        entity.setCurrencyPair(pair);
        entity.setValueDateCode(instrument.valueDateCode());
        jpaRepository.save(entity);
    }

    @Override
    public void delete(String pairCode) {
        jpaRepository.deleteAllByCurrencyPair_PairCode(pairCode);
    }

    @Override
    public Optional<FxSpot> find(String internalCode) {
        return jpaRepository.findById(internalCode)
                .map(entity -> new FxSpot(
                        CurrencyPairEntityMapper.toDomain(entity.getCurrencyPair()),
                        entity.getValueDateCode()
                ));
    }
}
