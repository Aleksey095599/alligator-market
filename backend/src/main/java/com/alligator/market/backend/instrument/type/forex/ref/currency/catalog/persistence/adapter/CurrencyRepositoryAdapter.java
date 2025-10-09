package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyCreateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyDeleteException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyUpdateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Адаптер, реализующий доменный порт {@link CurrencyRepository} через Spring Data JPA.
 */
@Repository
public class CurrencyRepositoryAdapter implements CurrencyRepository {

    private final CurrencyJpaRepository jpaRepository;

    /** Конструктор. */
    public CurrencyRepositoryAdapter(CurrencyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /** Создать новую валюту. */
    @Override
    public Currency create(Currency c) {
        Objects.requireNonNull(c, "currency must not be null");

        // Создаем JPA-сущность, используя специальный метод
        CurrencyEntity entity = CurrencyEntityMapper.newEntity(c);

        // Пробуем создать валюту или ловим наиболее вероятные ошибки и пробрасываем их выше
        try {
            CurrencyEntity saved = jpaRepository.saveAndFlush(entity);
            return CurrencyEntityMapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw new CurrencyCreateException(c.code(), ex);
        }
    }

    /** Обновить существующую валюту. */
    @Override
    public Currency update(Currency c) {
        Objects.requireNonNull(c, "currency must not be null");

        // Ищем JPA-сущность по коду валюты иначе бросаем ошибку
        CurrencyEntity e = jpaRepository.findByCode(c.code())
                .orElseThrow(() -> new CurrencyNotFoundException(c.code()));

        // Заполняем изменяемые поля из переданной модели
        CurrencyEntityMapper.apply(c, e);

        // Пробуем сохранить валюту или ловим наиболее вероятные ошибки и пробрасываем их выше
        try {
            CurrencyEntity saved = jpaRepository.saveAndFlush(e);
            return CurrencyEntityMapper.toDomain(saved);
        } catch (jakarta.validation.ConstraintViolationException | org.springframework.dao.DataAccessException ex) {
            throw new CurrencyUpdateException(c.code(), ex);
        }
    }

    /** Удалить валюту по коду. */
    @Override
    public void deleteByCode(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        // Ищем JPA-сущность по коду валюты иначе бросаем ошибку
        CurrencyEntity e = jpaRepository.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        // Пробуем удалить валюту или ловим наиболее вероятные ошибки и пробрасываем их выше
        try {
            jpaRepository.delete(e);
            jpaRepository.flush();
        } catch (org.springframework.dao.DataAccessException ex) {
            throw new CurrencyDeleteException(code, ex);
        }
    }

    /** Найти валюту по коду. */
    @Override
    public Optional<Currency> findByCode(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        return jpaRepository.findByCode(code)
                .map(CurrencyEntityMapper::toDomain);
    }

    /** Найти валюту по имени. */
    @Override
    public Optional<Currency> findByName(String name) {
        Objects.requireNonNull(name, "name must not be null");

        return jpaRepository.findByName(name)
                .map(CurrencyEntityMapper::toDomain);
    }

    /** Вернуть все валюты (отсортированы по коду). */
    @Override
    public List<Currency> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(CurrencyEntityMapper::toDomain)
                .toList();
    }
}
