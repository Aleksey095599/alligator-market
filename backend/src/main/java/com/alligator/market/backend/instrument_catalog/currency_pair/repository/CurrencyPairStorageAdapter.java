package com.alligator.market.backend.instrument_catalog.currency_pair.repository;

import com.alligator.market.backend.instrument_catalog.currency.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument_catalog.currency.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument_catalog.currency_pair.entity.CurrencyPairEntity;
import com.alligator.market.backend.instrument_catalog.currency_pair.entity.CurrencyPairEntityMapper;
import com.alligator.market.domain.instrument.currency_pair.CurrencyPair;
import com.alligator.market.domain.instrument.currency_pair.catalog.CurrencyPairStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер реализующий доменную модель хранилища валютных пар {@link CurrencyPairStorage} в контексте Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class CurrencyPairStorageAdapter implements CurrencyPairStorage {

    private final CurrencyPairJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyJpaRepository;

    @Override
    public String save(CurrencyPair pair) {
        CurrencyEntity c1 = currencyJpaRepository.findByCode(pair.base()).orElseThrow();
        CurrencyEntity c2 = currencyJpaRepository.findByCode(pair.quote()).orElseThrow();
        CurrencyPairEntity entity = new CurrencyPairEntity();
        CurrencyPairEntityMapper.toEntity(pair, c1, c2, entity);
        return jpaRepository.save(entity).getPairCode();
    }

    @Override
    public void delete(String base, String quote) {
        jpaRepository.findByPairCode(base+quote).ifPresent(jpaRepository::delete);
    }

    @Override
    public Optional<CurrencyPair> find(String base, String quote) {
        return jpaRepository.findByPairCode(base+quote).map(CurrencyPairEntityMapper::toDomain);
    }

    @Override
    public boolean existsByCurrency(String currencyCode) {
        return jpaRepository.existsByBase_CodeOrQuote_Code(currencyCode, currencyCode);
    }

    @Override
    public List<CurrencyPair> findAll() {
        return jpaRepository.findAll(Sort.by("pairCode")).stream()
                .map(CurrencyPairEntityMapper::toDomain)
                .toList();
    }

}
