package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntityMapper;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный порт репозитория инструментов типа FX_SPOT через Spring Data JPA.
 */
@Repository
public class FxSpotRepositoryAdapter implements FxSpotRepository {

    private final FxSpotJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final FxSpotEntityMapper mapper;

    /** Конструктор. */
    public FxSpotRepositoryAdapter(FxSpotJpaRepository jpaRepository,
                                   CurrencyJpaRepository currencyRepository,
                                   FxSpotEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    /** Создать новый FX_SPOT инструмент. */
    @Override
    public FxSpot create(FxSpot m) {
        Objects.requireNonNull(m, "FxSpot model must not be null");

        // Ищем JPA-сущности составных валют
        CurrencyEntity be = currencyRepository.findByCode(m.base().code())
                .orElseThrow(() -> new CurrencyNotFoundException(m.base().code()));
        CurrencyEntity qe = currencyRepository.findByCode(m.quote().code())
                .orElseThrow(() -> new CurrencyNotFoundException(m.quote().code()));

        // Создаем JPA-сущность, используя специальный метод
        FxSpotEntity entity = FxSpotEntityMapper.newEntity(m, be, qe);

        // Пробуем сохранить созданную сущность (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(entity);
            return mapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw
        }
    }



    @Override
    public void deleteByCode(String instrumentCode) {
        jpaRepository.deleteByCode(instrumentCode); // Удаляем по коду
    }

    @Override
    public Optional<FxSpot> findByCode(String instrumentCode) {
        return jpaRepository.findByCode(instrumentCode)
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
    public boolean existsByCurrencyCode(CurrencyCode currencyCode) {
        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
