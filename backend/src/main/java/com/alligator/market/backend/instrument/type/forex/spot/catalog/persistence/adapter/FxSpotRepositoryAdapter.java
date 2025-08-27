package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.backend.instrument.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.reference.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntityMapper;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный порт {@link FxSpotRepository} через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxSpotRepositoryAdapter implements FxSpotRepository {

    private final FxSpotJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final FxSpotEntityMapper mapper;

    @Override
    public void save(FxSpot fxSpot) {
        FxSpotEntity entity = jpaRepository.findByCode(fxSpot.getCode())
                .orElseGet(FxSpotEntity::new);
        CurrencyEntity base = currencyRepository.findByCode(fxSpot.baseCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(fxSpot.baseCurrency()));
        CurrencyEntity quote = currencyRepository.findByCode(fxSpot.quoteCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(fxSpot.quoteCurrency()));
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
