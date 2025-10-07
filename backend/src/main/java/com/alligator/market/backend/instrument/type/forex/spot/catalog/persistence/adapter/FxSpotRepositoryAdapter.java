package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntityMapper;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
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
        FxSpotEntity entity = jpaRepository.findByCode(fxSpot.code())
                .orElseGet(FxSpotEntity::new);
        // Получаем валюты (существование проверено в сервисе)
        CurrencyEntity base = currencyRepository.findByCode(fxSpot.base().code())
                .orElseThrow(() -> new IllegalStateException("Currency '%s' not found".formatted(fxSpot.base().code().value())));
        CurrencyEntity quote = currencyRepository.findByCode(fxSpot.quote().code())
                .orElseThrow(() -> new IllegalStateException("Currency '%s' not found".formatted(fxSpot.quote().code().value())));
        mapper.updateEntity(fxSpot, base, quote, entity);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(String code) {
        jpaRepository.deleteByCode(code); // Удаляем по коду
    }

    @Override
    public Optional<FxSpot> find(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }

    @Override
    public List<FxSpot> findAll() {
        return jpaRepository.findAll(Sort.by(Sort.Order.asc("code"))) // Сортируем по коду
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrency(Currency currency) {
        CurrencyCode code = currency.code();
        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(code, code);
    }
}
