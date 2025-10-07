package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный порт {@link CurrencyRepository} через Spring Data JPA.
 */
@Repository
public class CurrencyRepositoryAdapter implements CurrencyRepository {

    //
    private final CurrencyJpaRepository jpaRepository;

    /** Конструктор. */
    public CurrencyRepositoryAdapter(CurrencyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public String save(Currency currency) {
        CurrencyEntity entity = jpaRepository.findByCode(currency.code())
                .orElseGet(CurrencyEntity::new);
        CurrencyEntityMapper.updateEntity(currency, entity);
        return jpaRepository.save(entity).getCode().value();
    }

    @Override
    public void deleteByCode(String code) {
        jpaRepository.deleteByCode(CurrencyCode.of(code));
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return jpaRepository.findByCode(CurrencyCode.of(code)).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public Optional<Currency> findByName(String name) {
        return jpaRepository.findByName(name).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public List<Currency> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(CurrencyEntityMapper::toDomain)
                .toList();
    }
}
