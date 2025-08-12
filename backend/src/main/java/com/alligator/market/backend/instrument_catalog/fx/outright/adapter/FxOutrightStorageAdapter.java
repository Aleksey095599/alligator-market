package com.alligator.market.backend.instrument_catalog.fx.outright.adapter;

import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntityMapper;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairJpaRepository;
import com.alligator.market.backend.instrument_catalog.fx.outright.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument_catalog.fx.outright.jpa.FxOutrightJpaRepository;
import com.alligator.market.domain.instrument.type.fx.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.fx.outright.model.FxOutright;
import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер хранилища инструментов FX OUTRIGHT на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxOutrightStorageAdapter implements FxOutrightStorage {

    private final FxOutrightJpaRepository jpaRepository;
    private final CurrencyPairJpaRepository currencyPairJpaRepository;

    /** Внутренний источник операции. */
    private static final String VIA = "fx-outright-storage-adapter";

    @Override
    public void save(FxOutright instrument) {
        AuditContext previous = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, VIA));
        try {
            CurrencyPairEntity pair = currencyPairJpaRepository
                    .findByPairCode(instrument.currencyPair().pairCode())
                    .orElseThrow();

            FxOutrightEntity entity = new FxOutrightEntity();
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
    public Optional<FxOutright> find(String internalCode) {
        return jpaRepository.findById(internalCode)
                .map(entity -> new FxOutright(
                        CurrencyPairEntityMapper.toDomain(entity.getCurrencyPair()),
                        entity.getValueDateCode()
                ));
    }

    @Override
    public List<FxOutright> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> new FxOutright(
                        CurrencyPairEntityMapper.toDomain(entity.getCurrencyPair()),
                        entity.getValueDateCode()
                ))
                .toList();
    }
}
