package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.adapter;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.domain.common.exception.NotFoundException;
import com.alligator.market.domain.common.exception.ResourceUpdateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyCreateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
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

    /* JPA-репозиторий. */
    private final CurrencyJpaRepository jpaRepository;

    /** Конструктор. */
    public CurrencyRepositoryAdapter(CurrencyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /** Создать новую валюту. */
    @Override
    @Transactional
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
    @Transactional
    public Currency update(Currency c) {
        Objects.requireNonNull(c, "currency must not be null");

        // Ищем JPA-сущность по коду валюты (натуральный ключ)
        CurrencyEntity e = jpaRepository.findByCode(c.code())
                .orElseThrow(() -> new NotFoundException("Currency", c.code().value()));

        // Заполняем изменяемые поля из переданной модели
        CurrencyEntityMapper.apply(c, e);

        // Пробуем сохранить валюту или ловим наиболее вероятные ошибки и пробрасываем их выше
        try {
            CurrencyEntity saved = jpaRepository.saveAndFlush(e);
            return CurrencyEntityMapper.toDomain(saved);
        } catch (ConstraintViolationException ex) { // Bean Validation (до SQL)
            throw new ResourceUpdateException("Currency", ex.getMessage(), ex);
        } catch (DataAccessException ex) { // ORM/БД ошибки (перевод Spring)
            throw new CurrencyCreateException(c.code(), ex);
        }
    }

    @Override
    public void deleteByCode(String code) {
        jpaRepository.deleteByCode(CurrencyCode.of(code));
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return jpaRepository.findByCode(CurrencyCode.of(code)).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public Optional<Currency> findByName(String name) {
        return jpaRepository.findByName(name).map(CurrencyEntityMapper::toDomain);
    }

    @Override
    public List<Currency> findAll() {
        return jpaRepository.findAll(Sort.by("code")).stream()
                .map(CurrencyEntityMapper::toDomain)
                .toList();
    }
}
