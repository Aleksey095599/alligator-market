package com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyAlreadyExistsException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNameDuplicateException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.alligator.market.backend.infra.jooq.generated.tables.Currency.CURRENCY;

/**
 * jOOQ-адаптер доменного репозитория валют.
 */
public final class JooqCurrencyRepositoryAdapter implements CurrencyRepository {

    /* Имена UQ ограничений (должны совпадать с фактическими именами в DDL/схеме). */
    private static final String UQ_CURRENCY_NAME = "uq_currency_name";

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqCurrencyRepositoryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public Currency create(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        try {
            // Пытаемся вставить валюту по коду.
            Record createdRecord = dsl.insertInto(CURRENCY)
                    .set(CURRENCY.CODE, currency.code().value())
                    .set(CURRENCY.NAME, currency.name())
                    .set(CURRENCY.COUNTRY, currency.country())
                    .set(CURRENCY.FRACTION_DIGITS, currency.fractionDigits())
                    .onConflict(CURRENCY.CODE)
                    .doNothing()
                    .returning(
                            CURRENCY.CODE,
                            CURRENCY.NAME,
                            CURRENCY.COUNTRY,
                            CURRENCY.FRACTION_DIGITS
                    )
                    .fetchOne();
            // Если запись не создана — код уже существует.
            if (createdRecord == null) {
                throw new CurrencyAlreadyExistsException(currency.code());
            }
            // Возвращаем созданную доменную модель.
            return toDomain(createdRecord);
        } catch (DataIntegrityViolationException ex) {
            // Отдельно обрабатываем конфликт уникального имени.
            if (DbConstraintErrors.isViolationOf(ex, UQ_CURRENCY_NAME)) {
                throw new CurrencyNameDuplicateException(currency.name());
            }
            throw ex;
        }
    }

    @Override
    public Currency update(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        try {
            // Обновляем валюту по коду и сразу читаем результат.
            Optional<Currency> updatedCurrency = dsl.update(CURRENCY)
                    .set(CURRENCY.NAME, currency.name())
                    .set(CURRENCY.COUNTRY, currency.country())
                    .set(CURRENCY.FRACTION_DIGITS, currency.fractionDigits())
                    .where(CURRENCY.CODE.eq(currency.code().value()))
                    .returning(
                            CURRENCY.CODE,
                            CURRENCY.NAME,
                            CURRENCY.COUNTRY,
                            CURRENCY.FRACTION_DIGITS
                    )
                    .fetchOptional(this::toDomain);
            // Если запись не найдена — сигнализируем об этом.
            return updatedCurrency
                    .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));
        } catch (DataIntegrityViolationException ex) {
            // Отдельно обрабатываем конфликт уникального имени.
            if (DbConstraintErrors.isViolationOf(ex, UQ_CURRENCY_NAME)) {
                throw new CurrencyNameDuplicateException(currency.name());
            }
            throw ex;
        }
    }

    @Override
    public void deleteByCode(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        // Удаляем валюту по коду.
        int deletedRows = dsl.deleteFrom(CURRENCY)
                .where(CURRENCY.CODE.eq(code.value()))
                .execute();
        // Если удалять было нечего — валюты не существует.
        if (deletedRows == 0) {
            throw new CurrencyNotFoundException(code);
        }
    }

    @Override
    public Optional<Currency> findByCode(CurrencyCode code) {
        Objects.requireNonNull(code, "code must not be null");

        // Ищем одну валюту по коду.
        return dsl.select(
                        CURRENCY.CODE,
                        CURRENCY.NAME,
                        CURRENCY.COUNTRY,
                        CURRENCY.FRACTION_DIGITS
                )
                .from(CURRENCY)
                .where(CURRENCY.CODE.eq(code.value()))
                .fetchOptional(this::toDomain);
    }

    @Override
    public List<Currency> findAll() {
        // Читаем весь справочник валют с детерминированной сортировкой.
        return dsl.select(
                        CURRENCY.CODE,
                        CURRENCY.NAME,
                        CURRENCY.COUNTRY,
                        CURRENCY.FRACTION_DIGITS
                )
                .from(CURRENCY)
                .orderBy(CURRENCY.CODE.asc())
                .fetch(this::toDomain);
    }

    /* Маппинг записи jOOQ в доменную модель Currency. */
    private Currency toDomain(Record record) {
        Objects.requireNonNull(record, "record must not be null");

        return new Currency(
                CurrencyCode.of(record.get(CURRENCY.CODE)),
                record.get(CURRENCY.NAME),
                record.get(CURRENCY.COUNTRY),
                record.get(CURRENCY.FRACTION_DIGITS)
        );
    }
}
