package com.alligator.market.backend.instrument_catalog.fx.reference.currency.adapter;

import com.alligator.market.backend.instrument_catalog.fx.reference.currency.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency.jpa.CurrencyEntityMapper;
import com.alligator.market.domain.instrument.type.fx.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.fx.reference.currency.catalog.CurrencyStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер хранилища валют на Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class CurrencyStorageAdapter implements CurrencyStorage {

    private final CurrencyJpaRepository jpaRepository;

    @Override
    public String save(Currency currency) {
        CurrencyEntity entity = jpaRepository.findByCode(currency.code())
                .orElseGet(CurrencyEntity::new);
        CurrencyEntityMapper.toEntity(currency, entity);
        return jpaRepository.save(entity).getCode();
    }

    @Override
    public void deleteByCode(String code) {
        jpaRepository.findByCode(code).ifPresent(jpaRepository::delete);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return jpaRepository.findByCode(code).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public Optional<Currency> findByName(String name) {
        return jpaRepository.findByName(name).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public Optional<Currency> findByCountry(String country) {
        return jpaRepository.findByCountry(country).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public List<Currency> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(CurrencyEntityMapper::toDomain)
                .toList();
    }
}
