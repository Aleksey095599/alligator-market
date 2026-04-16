package com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.repository;

import com.alligator.market.backend.common.persistence.jpa.constraint.DbErrors;
import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa.FxSpotEntityMapper;
import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.*;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Адаптер доменного репозитория инструментов FOREX_SPOT (в контексте Spring Data JPA).
 */
@RequiredArgsConstructor
public class FxSpotRepositoryAdapter implements FxSpotRepository {

    /* Имя UQ ограничения (должно совпадать с фактическим именем в DDL/схеме). */
    private static final String UQ_INSTRUMENT_CODE = "uq_instrument_code";

    /* JPA-репозиторий FOREX_SPOT + доменный репозиторий валют для existence-check. */
    private final FxSpotJpaRepository jpaRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public FxSpot create(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Проверяем существование составных валют (бизнес‑ошибки при отсутствии)
        ensureCurrencyExists(fxSpot.base().code());
        ensureCurrencyExists(fxSpot.quote().code());

        // Создаем JPA-сущность, используя специальный метод
        FxSpotEntity entity = FxSpotEntityMapper.toNewEntity(fxSpot);

        // Пробуем сохранить созданную сущность и маппим наиболее вероятные ошибки
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(entity);
            return FxSpotEntityMapper.toDomain(saved); // Успех
        } catch (DataIntegrityViolationException ex) {
            if (DbErrors.isViolationOf(ex, UQ_INSTRUMENT_CODE)) {
                throw new FxSpotAlreadyExistsException(fxSpot.instrumentCode()); // <-- Бизнес‑ошибка: дубликат
            }
            throw new FxSpotCreateException(fxSpot.instrumentCode(), ex); // <-- Иные ошибки целостности
        } catch (ConstraintViolationException ex) {
            // Bean Validation на entity: @NotNull/@Min/@Max и т.п. --> техническая ошибка создания
            throw new FxSpotCreateException(fxSpot.instrumentCode(), ex);
        } catch (DataAccessException ex) {
            // Любые прочие ошибки доступа к данным (тайм-ауты, deadlock, Bad SQL и т.д.)
            throw new FxSpotCreateException(fxSpot.instrumentCode(), ex);
        }
    }

    @Override
    public FxSpot update(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot model must not be null");

        // Ищем JPA-сущность инструмента FOREX_SPOT к обновлению
        FxSpotEntity e = jpaRepository.findByCode(fxSpot.instrumentCode().value())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Заполняем изменяемые поля из переданной модели
        FxSpotEntityMapper.applyToEntity(fxSpot, e);

        // Пробуем сохранить обновленную сущность и маппим наиболее вероятные ошибки
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(e);
            return FxSpotEntityMapper.toDomain(saved); // <-- Успех
        } catch (DataIntegrityViolationException ex) {
            // Любые ошибки целостности данных (уникальности/ограничения на уровне БД)
            // Для update бизнес-уникальность не меняется, поэтому маппим в техническую ошибку обновления
            throw new FxSpotUpdateException(fxSpot.instrumentCode(), ex);
        } catch (ConstraintViolationException ex) {
            // Bean Validation на entity: @NotNull/@Min/@Max и т.п. --> техническая ошибка обновления
            throw new FxSpotUpdateException(fxSpot.instrumentCode(), ex);
        } catch (DataAccessException ex) {
            // Прочие ошибки доступа к данным (тайм-ауты, deadlock, Bad SQL и т.д.)
            throw new FxSpotUpdateException(fxSpot.instrumentCode(), ex);
        }
    }

    @Override
    public void deleteByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        // Строковое значение кода инструмента для запроса в БД
        String codeValue = instrumentCode.value();

        // Ищем JPA-сущность по коду инструмента иначе бросаем ошибку
        FxSpotEntity e = jpaRepository.findByCode(codeValue)
                .orElseThrow(() -> new FxSpotNotFoundException(instrumentCode));

        // Пробуем удалить запись (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            jpaRepository.delete(e);
            jpaRepository.flush();
        } catch (DataAccessException ex) {
            throw new FxSpotDeleteException(instrumentCode, ex);
        }
    }

    @Override
    public Optional<FxSpot> findByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        // Строковое значение кода инструмента для запроса в БД
        String codeValue = instrumentCode.value();

        return jpaRepository.findByCode(codeValue)
                .map(FxSpotEntityMapper::toDomain);
    }

    @Override
    public List<FxSpot> findAll() {

        return jpaRepository.findAll(Sort.by(Sort.Order.asc("code")))
                .stream()
                .map(FxSpotEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrencyCode(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return jpaRepository.existsByBaseCurrencyCodeOrQuoteCurrencyCode(currencyCode, currencyCode);
    }

    /* Проверяет наличие валюты в reference-справочнике. */
    private void ensureCurrencyExists(CurrencyCode code) {
        Objects.requireNonNull(code, "currency code must not be null");
        if (currencyRepository.findByCode(code).isEmpty()) {
            throw new CurrencyNotFoundException(code);
        }
    }
}
