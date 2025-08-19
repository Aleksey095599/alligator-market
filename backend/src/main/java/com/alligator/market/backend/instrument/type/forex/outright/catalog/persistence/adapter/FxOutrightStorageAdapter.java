package com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa.FxOutrightJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.persistence.jpa.FxOutrightEntityMapper;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.FxOutrightCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный контракт {@link FxOutrightStorage}
 * с использованием Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxOutrightStorageAdapter implements FxOutrightStorage {

    private final FxOutrightJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final FxOutrightEntityMapper mapper;

    @Override
    public void save(FxOutright fxOutright) {
        FxOutrightEntity entity = jpaRepository.findByCode(fxOutright.code())
                .orElseGet(FxOutrightEntity::new);
        CurrencyEntity base = currencyRepository.findByCode(fxOutright.baseCurrency())
                .orElseThrow(() -> new FxOutrightCurrencyNotFoundException(fxOutright.baseCurrency()));
        CurrencyEntity quote = currencyRepository.findByCode(fxOutright.quoteCurrency())
                .orElseThrow(() -> new FxOutrightCurrencyNotFoundException(fxOutright.quoteCurrency()));
        mapper.updateEntity(fxOutright, base, quote, entity);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(String code) {
        jpaRepository.findByCode(code)
                .ifPresent(e -> jpaRepository.deleteById(e.getId())); // Удаляем по ID
    }

    @Override
    public Optional<FxOutright> find(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }

    @Override
    public List<FxOutright> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrency(String currencyCode) {
        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
