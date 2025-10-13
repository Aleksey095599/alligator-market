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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный порт репозитория инструментов типа FX_SPOT через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxSpotRepositoryAdapter implements FxSpotRepository {

    private final FxSpotJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final FxSpotEntityMapper mapper;

    @Override
    public FxSpot create(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем JPA-сущности составных валют
        CurrencyEntity baseEntity = currencyRepository.findByCode(fxSpot.base().code())
                .orElseThrow(() -> new CurrencyNotFoundException(fxSpot.base().code()));
        CurrencyEntity quoteEntity = currencyRepository.findByCode(fxSpot.quote().code())
                .orElseThrow(() -> new CurrencyNotFoundException(fxSpot.quote().code()));

        // Создаем JPA-сущность, используя специальный метод
        FxSpotEntity entity = FxSpotEntityMapper.newEntity(fxSpot, baseEntity, quoteEntity);

        // Пробуем сохранить созданную сущность (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(entity);
            return mapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw new FxSpotCreateException(fxSpot.instrumentCode(), ex);
        }
    }

    @Override
    public FxSpot update(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot model must not be null");

        // Ищем JPA-сущность инструмента FX_SPOT к обновлению
        FxSpotEntity e = jpaRepository.findByCode(fxSpot.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Заполняем изменяемые поля из переданной модели
        FxSpotEntityMapper.apply(fxSpot, e);

        // Пробуем сохранить обновленную сущность (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(e);
            return mapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw new FxSpotUpdateException(fxSpot.instrumentCode(), ex);
        }
    }

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

    @Override
    public Optional<FxSpot> findByCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return jpaRepository.findByCode(instrumentCode)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByInstrumentCode(String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return jpaRepository.existsByCode(instrumentCode);
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
