package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntityMapper;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCreateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotDeleteException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotUpdateException;
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
    public FxSpot create(FxSpot model) {
        Objects.requireNonNull(model, "FxSpot model must not be null");

        // Ищем JPA-сущности составных валют
        CurrencyEntity baseEntity = currencyRepository.findByCode(model.base().code())
                .orElseThrow(() -> new CurrencyNotFoundException(model.base().code()));
        CurrencyEntity quoteEntity = currencyRepository.findByCode(model.quote().code())
                .orElseThrow(() -> new CurrencyNotFoundException(model.quote().code()));

        // Создаем JPA-сущность, используя специальный метод
        FxSpotEntity entity = FxSpotEntityMapper.newEntity(model, baseEntity, quoteEntity);

        // Пробуем сохранить созданную сущность (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(entity);
            return mapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw new FxSpotCreateException(model.instrumentCode(), ex);
        }
    }

    /** Обновить существующий FX_SPOT инструмент. */
    @Override
    public FxSpot update(FxSpot m) {
        Objects.requireNonNull(m, "FxSpot model must not be null");

        // Ищем JPA-сущность инструмента FX_SPOT к обновлению
        FxSpotEntity e = jpaRepository.findByCode(m.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(m.instrumentCode()));

        // Заполняем изменяемые поля из переданной модели
        FxSpotEntityMapper.apply(m, e);

        // Пробуем сохранить обновленную сущность (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(e);
            return mapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw new FxSpotUpdateException(m.instrumentCode(), ex);
        }
    }

    /** Удалить инструмент FX_SPOT по коду. */
    @Override
    public void deleteByCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Ищем JPA-сущность по коду инструмента иначе бросаем ошибку
        FxSpotEntity e = jpaRepository.findByCode(instrumentCode)
                        .orElseThrow(() -> new FxSpotNotFoundException(instrumentCode));

        // Пробуем удалить запись (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            jpaRepository.delete(e);
            jpaRepository.flush();
        } catch (org.springframework.dao.DataAccessException ex) {
            throw new FxSpotDeleteException(instrumentCode, ex);
        }
    }

    /** Найти инструмент FX_SPOT по коду. */
    @Override
    public Optional<FxSpot> findByCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return jpaRepository.findByCode(instrumentCode)
                .map(mapper::toDomain);
    }

    /** Вернуть все инструменты FX_SPOT. */
    @Override
    public List<FxSpot> findAll() {
        return jpaRepository.findAll(Sort.by(Sort.Order.asc("code"))) // Сортируем по коду
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** Проверить, используется ли заданный код валюты хотя бы в одном инструменте. */
    @Override
    public boolean existsByCurrencyCode(CurrencyCode currencyCode) {
        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
