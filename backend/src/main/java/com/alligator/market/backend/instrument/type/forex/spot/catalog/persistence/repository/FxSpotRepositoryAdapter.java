package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.repository;

import com.alligator.market.backend.common.persistence.jpa.constraint.DbErrors;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntity;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotEntityAssembler;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.type.forex.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.exception.*;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
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
 * Адаптер, реализующий доменный порт репозитория инструментов типа FX_SPOT через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class FxSpotRepositoryAdapter implements FxSpotRepository {

    /* Натуральный ключ инструмента (совпадает с бизнес‑идентичностью). */
    private static final String UQ_INSTRUMENT_CODE = "uq_instrument_code";

    /* JPA-репозитории для инструментов FX_SPOT и валют. */
    private final FxSpotJpaRepository jpaRepository;
    private final CurrencyJpaRepository currencyRepository;

    @Override
    public FxSpot create(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Ищем JPA-сущности составных валют (бизнес‑ошибки при отсутствии)
        CurrencyEntity baseEntity = currencyRepository.findByCode(fxSpot.base().code())
                .orElseThrow(() -> new CurrencyNotFoundException(fxSpot.base().code()));
        CurrencyEntity quoteEntity = currencyRepository.findByCode(fxSpot.quote().code())
                .orElseThrow(() -> new CurrencyNotFoundException(fxSpot.quote().code()));

        // Создаем JPA-сущность, используя специальный метод
        FxSpotEntity entity = FxSpotEntityAssembler.newEntity(fxSpot, baseEntity, quoteEntity);

        // Пробуем сохранить созданную сущность и маппим наиболее вероятные ошибки
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(entity);
            return FxSpotEntityAssembler.toDomain(saved); // Успех
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

        // Ищем JPA-сущность инструмента FX_SPOT к обновлению
        FxSpotEntity e = jpaRepository.findByCode(fxSpot.instrumentCode().value())
                .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));

        // Заполняем изменяемые поля из переданной модели
        FxSpotEntityAssembler.apply(fxSpot, e);

        // Пробуем сохранить обновленную сущность и маппим наиболее вероятные ошибки
        try {
            FxSpotEntity saved = jpaRepository.saveAndFlush(e);
            return FxSpotEntityAssembler.toDomain(saved); // <-- Успех
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
                .map(FxSpotEntityAssembler::toDomain);
    }

    @Override
    public List<FxSpot> findAll() {

        return jpaRepository.findAll(Sort.by(Sort.Order.asc("code")))
                .stream()
                .map(FxSpotEntityAssembler::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCurrencyCode(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return jpaRepository.existsByBaseCurrency_CodeOrQuoteCurrency_Code(currencyCode, currencyCode);
    }
}
