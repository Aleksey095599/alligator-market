package com.alligator.market.backend.instrument_catalog.fx.spot.adapter;

import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntityMapper;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.backend.instrument_catalog.fx.spot.jpa.FxSpotInstrumentEntity;
import com.alligator.market.backend.instrument_catalog.fx.spot.jpa.FxSpotInstrumentJpaRepository;
import com.alligator.market.domain.instrument.type.fx.spot.catalog.FxSpotInstrumentStorage;
import com.alligator.market.domain.instrument.type.fx.spot.model.FxSpot;
import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер хранилища инструментов FX SPOT на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxSpotInstrumentStorageAdapter implements FxSpotInstrumentStorage {

    private final FxSpotInstrumentJpaRepository jpaRepository;
    private final CurrencyPairJpaRepository currencyPairJpaRepository;

    /** Внутренний источник операции. */
    private static final String VIA = "fx-spot-instrument-storage-adapter";

    @Override
    public void save(FxSpot instrument) {
        AuditContext previous = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, VIA));
        try {
            CurrencyPairEntity pair = currencyPairJpaRepository
                    .findByPairCode(instrument.currencyPair().pairCode())
                    .orElseThrow();

            FxSpotInstrumentEntity entity = new FxSpotInstrumentEntity();
            entity.setInternalCode(instrument.internalCode());
            entity.setCurrencyPair(pair);
            entity.setValueDateCode(instrument.valueDateCode());
            jpaRepository.save(entity); // Сохраняем entity в БД
        } finally {
            AuditContextHolder.set(previous);
        }
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

    @Override
    public List<FxSpot> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> new FxSpot(
                        CurrencyPairEntityMapper.toDomain(entity.getCurrencyPair()),
                        entity.getValueDateCode()
                ))
                .toList();
    }
}
