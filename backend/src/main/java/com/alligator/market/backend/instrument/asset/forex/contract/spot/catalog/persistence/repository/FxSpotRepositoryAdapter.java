package com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.persistence.repository;

import com.alligator.market.backend.common.persistence.jpa.constraint.DbErrors;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.persistence.jpa.FxSpotEntityMapper;
import com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.exception.*;
import com.alligator.market.domain.instrument.model.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.InstrumentFxSpot;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.repository.FxSpotRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Адаптер доменного репозитория инструментов FOREX_SPOT (в контексте Spring Data JPA).
 */
@Repository
@RequiredArgsConstructor
public class FxSpotRepositoryAdapter implements FxSpotRepository {

    /* Имя UQ ограничения (должно совпадать с фактическим именем в DDL/схеме). */
    private static final String UQ_INSTRUMENT_CODE = "uq_instrument_code";

    /* JPA-репозитории для инструментов FOREX_SPOT и валют. */
    private final FxSpotJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyJpaRepository;

    @Override
    public InstrumentFxSpot create(InstrumentFxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем JPA-сущности составных валют (бизнес‑ошибки при отсутствии)
        CurrencyEntity baseEntity = currencyJpaRepository.findByCode(fxSpot.base().code())
                .orElseThrow(() -> new CurrencyNotFoundException(fxSpot.base().code()));
        CurrencyEntity quoteEntity = currencyJpaRepository.findByCode(fxSpot.quote().code())
                .orElseThrow(() -> new CurrencyNotFoundException(fxSpot.quote().code()));

        // Создаем JPA-сущность, используя специальный метод
        FxSpotEntity entity = FxSpotEntityMapper.toNewEntity(fxSpot, baseEntity, quoteEntity);

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
    public InstrumentFxSpot update(InstrumentFxSpot fxSpot) {
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
    public Optional<InstrumentFxSpot> findByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        // Строковое значение кода инструмента для запроса в БД
        String codeValue = instrumentCode.value();

        return jpaRepository.findByCode(codeValue)
                .map(FxSpotEntityMapper::toDomain);
    }

    @Override
    public List<InstrumentFxSpot> findAll() {

        return jpaRepository.findAll(Sort.by(Sort.Order.asc("code")))
                .stream()
                .map(FxSpotEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrencyCode(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
