package com.alligator.market.backend.instrument.type.forex.outright.catalog.adapter;

import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.mapper.FxOutrightEntityMapper;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.FxOutrightCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер хранилища FX_OUTRIGHT на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxOutrightStorageAdapter implements FxOutrightStorage {

    private final FxOutrightJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final FxOutrightEntityMapper mapper;

    @Override
    public void save(FxOutright instrument) {
        FxOutrightEntity entity = jpaRepository.findByCode(instrument.code())
                .orElseGet(FxOutrightEntity::new);

        CurrencyEntity base = currencyRepository.findByCode(instrument.baseCurrency())
                .orElseThrow(() -> new FxOutrightCurrencyNotFoundException(instrument.baseCurrency()));
        CurrencyEntity quote = currencyRepository.findByCode(instrument.quoteCurrency())
                .orElseThrow(() -> new FxOutrightCurrencyNotFoundException(instrument.quoteCurrency()));

        mapper.updateEntity(instrument, base, quote, entity);
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
