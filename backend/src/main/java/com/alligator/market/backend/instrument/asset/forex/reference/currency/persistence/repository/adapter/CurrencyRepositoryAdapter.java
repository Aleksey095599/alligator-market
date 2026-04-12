package com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.repository.adapter;

import com.alligator.market.backend.common.persistence.jpa.constraint.DbErrors;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.exception.*;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
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
 * Адаптер доменного репозитория валют (в контексте Spring Data JPA).
 */
@Repository
@RequiredArgsConstructor
public class CurrencyRepositoryAdapter implements CurrencyRepository {

    /* Имена UQ ограничений (должны совпадать с фактическими именами в DDL/схеме). */
    private static final String UQ_CURRENCY_CODE = "uq_currency_code";
    private static final String UQ_CURRENCY_NAME = "uq_currency_name";

    /* JPA-репозиторий. */
    private final CurrencyJpaRepository jpaRepository;

    @Override
    public Currency create(Currency c) {
        Objects.requireNonNull(c, "currency must not be null");

        // Создаем JPA-сущность, используя специальный метод
        CurrencyEntity entity = CurrencyEntityMapper.newEntity(c);

        // Пробуем сохранить созданную сущность и маппим наиболее вероятные ошибки
        try {
            CurrencyEntity saved = jpaRepository.saveAndFlush(entity);
            return CurrencyEntityMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            // Нарушение натурального ключа (code) --> AlreadyExists
            if (DbErrors.isViolationOf(ex, UQ_CURRENCY_CODE)) {
                throw new CurrencyAlreadyExistsException(c.code());
            }
            // Нарушение уникальности имени --> NameDuplicate
            if (DbErrors.isViolationOf(ex, UQ_CURRENCY_NAME)) {
                throw new CurrencyNameDuplicateException(c.name());
            }
            // Иные ошибки целостности --> техническая ошибка создания
            throw new CurrencyCreateException(c.code(), ex);
        } catch (ConstraintViolationException ex) {
            // Bean Validation (аннотации на entity)
            throw new CurrencyCreateException(c.code(), ex);
        } catch (DataAccessException ex) {
            // Прочие ошибки доступа к данным
            throw new CurrencyCreateException(c.code(), ex);
        }
    }

    @Override
    public Currency update(Currency c) {
        Objects.requireNonNull(c, "currency must not be null");

        // Ищем JPA-сущность валюты к обновлению
        CurrencyEntity e = jpaRepository.findByCode(c.code())
                .orElseThrow(() -> new CurrencyNotFoundException(c.code()));

        // Заполняем изменяемые поля из переданной модели
        CurrencyEntityMapper.apply(c, e);

        // Пробуем сохранить обновленную сущность и маппим наиболее вероятные ошибки
        try {
            CurrencyEntity saved = jpaRepository.saveAndFlush(e);
            return CurrencyEntityMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            // Для update код неизменяем (натуральный ключ), значит ловим только UQ по имени
            if (DbErrors.isViolationOf(ex, UQ_CURRENCY_NAME)) {
                throw new CurrencyNameDuplicateException(c.name());
            }
            // Иные ошибки целостности --> техническая ошибка обновления
            throw new CurrencyUpdateException(c.code(), ex);
        } catch (ConstraintViolationException ex) {
            // Bean Validation (аннотации на entity)
            throw new CurrencyUpdateException(c.code(), ex);
        } catch (DataAccessException ex) {
            // Прочие ошибки доступа к данным
            throw new CurrencyUpdateException(c.code(), ex);
        }
    }

    @Override
    public void deleteByCode(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        // Ищем JPA-сущность по коду валюты иначе бросаем ошибку
        CurrencyEntity e = jpaRepository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        // Пробуем удалить запись (ловим наиболее вероятные ошибки и пробрасываем их выше)
        try {
            jpaRepository.delete(e);
            jpaRepository.flush();
        } catch (DataAccessException ex) {
            throw new CurrencyDeleteException(code, ex);
        }
    }

    @Override
    public Optional<Currency> findByCode(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        return jpaRepository.findByCode(code).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public List<Currency> findAll() {

        return jpaRepository.findAll(Sort.by("code"))
                .stream()
                .map(CurrencyEntityMapper::toDomain)
                .toList();
    }
}
