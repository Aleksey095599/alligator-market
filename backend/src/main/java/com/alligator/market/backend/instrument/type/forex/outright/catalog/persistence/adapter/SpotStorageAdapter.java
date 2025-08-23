package com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa.FxOutrightJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa.FxOutrightEntityMapper;
import com.alligator.market.domain.instrument.type.forex.spot.storage.SpotStorage;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный контракт {@link SpotStorage}
 * с использованием Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class SpotStorageAdapter implements SpotStorage {

    private final FxOutrightJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final FxOutrightEntityMapper mapper;

    @Override
    public void save(FxSpot fxSpot) {
        FxOutrightEntity entity = jpaRepository.findByCode(fxSpot.getCode())
                .orElseGet(FxOutrightEntity::new);
        CurrencyEntity base = currencyRepository.findByCode(fxSpot.baseCurrency())
                .orElseThrow(() -> new FxOutrightCurrencyNotFoundException(fxSpot.baseCurrency()));
        CurrencyEntity quote = currencyRepository.findByCode(fxSpot.quoteCurrency())
                .orElseThrow(() -> new FxOutrightCurrencyNotFoundException(fxSpot.quoteCurrency()));
        mapper.updateEntity(fxSpot, base, quote, entity);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(String code) {
        jpaRepository.findByCode(code)
                .ifPresent(e -> jpaRepository.deleteById(e.getId())); // Удаляем по ID
    }

    @Override
    public Optional<FxSpot> find(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }

    @Override
    public List<FxSpot> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrency(String currencyCode) {
        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
