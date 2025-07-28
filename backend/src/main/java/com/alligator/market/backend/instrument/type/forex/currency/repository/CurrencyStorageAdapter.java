package com.alligator.market.backend.instrument.type.forex.currency.repository;

import com.alligator.market.backend.instrument.type.forex.currency.entity.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.currency.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.CurrencyStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер реализующий доменную модель хранилища валют
 * {@link CurrencyStorage} в контексте Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class CurrencyStorageAdapter implements CurrencyStorage {

    private final CurrencyJpaRepository jpaRepository;

    @Override
    public String save(Currency currency) {
        CurrencyEntity entity = jpaRepository.findByCode(currency.code())
                .orElseGet(CurrencyEntity::new);
        entity.setCode(currency.code());
        entity.setName(currency.name());
        entity.setCountry(currency.country());
        entity.setDecimal(currency.decimal());
        return jpaRepository.save(entity).getCode();
    }

    @Override
    public void deleteByCode(String code) {
        jpaRepository.findByCode(code).ifPresent(jpaRepository::delete);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public Optional<Currency> findByName(String name) {
        return jpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public Optional<Currency> findByCountry(String country) {
        return jpaRepository.findByCountry(country).map(this::toDomain);
    }

    @Override
    public List<Currency> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(this::toDomain)
                .toList();
    }

    private Currency toDomain(CurrencyEntity entity) {
        return new Currency(
                entity.getCode(),
                entity.getName(),
                entity.getCountry(),
                entity.getDecimal()
        );
    }
}
