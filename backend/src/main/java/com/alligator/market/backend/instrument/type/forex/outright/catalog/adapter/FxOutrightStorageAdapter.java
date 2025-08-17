package com.alligator.market.backend.instrument.type.forex.outright.catalog.adapter;

import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightEntityMapper;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightJpaRepository;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyJpaRepository;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception.CurrencyNotFoundException;
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

    @Override
    public void save(FxOutright instrument) {
        FxOutrightEntity entity = jpaRepository.findByInstrumentCode(instrument.code())
                .orElseGet(FxOutrightEntity::new);

        CurrencyEntity base = currencyRepository.findByCode(instrument.baseCurrency())
                .orElseThrow(() -> new CurrencyNotFoundException(instrument.baseCurrency()));
        CurrencyEntity quote = currencyRepository.findByCode(instrument.quoteCurrency())
                .orElseThrow(() -> new CurrencyNotFoundException(instrument.quoteCurrency()));

        FxOutrightEntityMapper.toEntity(instrument, entity, base, quote);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(String internalCode) {
        jpaRepository.findByInstrumentCode(internalCode)
                .ifPresent(jpaRepository::delete);
    }

    @Override
    public Optional<FxOutright> find(String internalCode) {
        return jpaRepository.findByInstrumentCode(internalCode)
                .map(FxOutrightEntityMapper::toDomain);
    }

    @Override
    public List<FxOutright> findAll() {
        return jpaRepository.findAll(Sort.by("instrument.code")).stream()
                .map(FxOutrightEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrency(String currencyCode) {
        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
