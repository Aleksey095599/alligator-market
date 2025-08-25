package com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный порт {@link CurrencyRepository} с помощью Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class CurrencyRepositoryAdapter implements CurrencyRepository {

    private final CurrencyJpaRepository jpaRepository;
    private final CurrencyEntityMapper mapper;

    @Override
    public String save(Currency currency) {
        CurrencyEntity entity = jpaRepository.findByCode(currency.code())
                .orElseGet(CurrencyEntity::new);
        mapper.updateEntity(currency, entity);
        return jpaRepository.save(entity).getCode();
    }

    @Override
    public void deleteByCode(String code) {
        jpaRepository.deleteByCode(code);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public Optional<Currency> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public List<Currency> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
