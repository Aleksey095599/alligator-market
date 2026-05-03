package com.alligator.market.backend.instrument.asset.forex.fxspot.persistence.jooq.repository;

import com.alligator.market.backend.common.persistence.constraint.DbConstraintErrors;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotAlreadyExistsException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotCreateException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotDeleteException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotInUseException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotNotFoundException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotUpdateException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectFieldOrAsterisk;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.alligator.market.backend.infra.jooq.generated.tables.Currency.CURRENCY;
import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentFxSpot.INSTRUMENT_FX_SPOT;
import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentRegistry.INSTRUMENT_REGISTRY;

/**
 * jOOQ-адаптер доменного репозитория инструментов FOREX_SPOT.
 */
public final class JooqFxSpotRepositoryAdapter implements FxSpotRepository {

    /* Имя FK-ограничения, блокирующего удаление используемого FX_SPOT инструмента. */
    private static final String FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT =
            "fk_market_data_source_plan_instrument";

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqFxSpotRepositoryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public FxSpot create(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        // Проверяем, что обе валюты уже заведены в справочнике.
        ensureCurrencyExists(fxSpot.base().code());
        ensureCurrencyExists(fxSpot.quote().code());

        try {
            // Создаём instrument и его FX_SPOT-часть в одной транзакции.
            return dsl.transactionResult(configuration -> {
                DSLContext tx = configuration.dsl();
                InstrumentCode instrumentCode = fxSpot.instrumentCode();

                // Регистрируем код инструмента.
                int insertedRegistryRows = tx.insertInto(INSTRUMENT_REGISTRY)
                        .set(INSTRUMENT_REGISTRY.CODE, instrumentCode.value())
                        .onConflict(INSTRUMENT_REGISTRY.CODE)
                        .doNothing()
                        .execute();

                if (insertedRegistryRows == 0) {
                    throw new FxSpotAlreadyExistsException(instrumentCode);
                }

                // Сохраняем параметры FX_SPOT.
                int insertedFxSpotRows = tx.insertInto(INSTRUMENT_FX_SPOT)
                        .set(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE, instrumentCode.value())
                        .set(INSTRUMENT_FX_SPOT.BASE_CURRENCY, fxSpot.base().code().value())
                        .set(INSTRUMENT_FX_SPOT.QUOTE_CURRENCY, fxSpot.quote().code().value())
                        .set(INSTRUMENT_FX_SPOT.TENOR, fxSpot.tenor().name())
                        .set(INSTRUMENT_FX_SPOT.QUOTE_FRACTION_DIGITS, fxSpot.defaultQuoteFractionDigits())
                        .onConflict(
                                INSTRUMENT_FX_SPOT.BASE_CURRENCY,
                                INSTRUMENT_FX_SPOT.QUOTE_CURRENCY,
                                INSTRUMENT_FX_SPOT.TENOR
                        )
                        .doNothing()
                        .execute();

                if (insertedFxSpotRows == 0) {
                    throw new FxSpotAlreadyExistsException(instrumentCode);
                }

                // Читаем обратно сохранённую сущность.
                return findByCode(tx, instrumentCode)
                        .orElseThrow(() -> new IllegalStateException(
                                "Created FX_SPOT instrument was not found after insert"
                        ));
            });
        } catch (DataAccessException ex) {
            throw new FxSpotCreateException(fxSpot.instrumentCode(), ex);
        }
    }

    @Override
    public FxSpot update(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        try {
            // Обновляем только изменяемые поля FX_SPOT.
            int updatedRows = dsl.update(INSTRUMENT_FX_SPOT)
                    .set(INSTRUMENT_FX_SPOT.QUOTE_FRACTION_DIGITS, fxSpot.defaultQuoteFractionDigits())
                    .where(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE.eq(fxSpot.instrumentCode().value()))
                    .execute();

            if (updatedRows == 0) {
                throw new FxSpotNotFoundException(fxSpot.instrumentCode());
            }

            // Возвращаем актуальное состояние из БД.
            return findByCode(fxSpot.instrumentCode())
                    .orElseThrow(() -> new FxSpotNotFoundException(fxSpot.instrumentCode()));
        } catch (DataAccessException ex) {
            throw new FxSpotUpdateException(fxSpot.instrumentCode(), ex);
        }
    }

    @Override
    public void deleteByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        try {
            // Удаляем связанные записи в корректном порядке внутри транзакции.
            dsl.transaction(configuration -> {
                DSLContext tx = configuration.dsl();

                // Сначала удаляем запись FX_SPOT.
                int deletedFxSpotRows = tx.deleteFrom(INSTRUMENT_FX_SPOT)
                        .where(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE.eq(instrumentCode.value()))
                        .execute();

                if (deletedFxSpotRows == 0) {
                    throw new FxSpotNotFoundException(instrumentCode);
                }

                // Затем удаляем запись из общего реестра инструментов.
                int deletedRegistryRows = tx.deleteFrom(INSTRUMENT_REGISTRY)
                        .where(INSTRUMENT_REGISTRY.CODE.eq(instrumentCode.value()))
                        .execute();

                if (deletedRegistryRows == 0) {
                    throw new IllegalStateException("FX_SPOT registry row was not found during delete");
                }
            });
        } catch (DataAccessException ex) {
            if (DbConstraintErrors.isViolationOf(ex, FK_MARKET_DATA_SOURCE_PLAN_INSTRUMENT)) {
                throw new FxSpotInUseException(instrumentCode);
            }
            throw new FxSpotDeleteException(instrumentCode, ex);
        }
    }

    @Override
    public Optional<FxSpot> findByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return findByCode(dsl, instrumentCode);
    }

    @Override
    public List<FxSpot> findAll() {
        var baseCurrencyRef = CURRENCY.as("base_currency_ref");
        var quoteCurrencyRef = CURRENCY.as("quote_currency_ref");

        // Получаем все FX_SPOT и сразу собираем доменные объекты.
        return dsl.select(fxSpotSelectFields(baseCurrencyRef, quoteCurrencyRef))
                .from(INSTRUMENT_FX_SPOT)
                .join(INSTRUMENT_REGISTRY)
                .on(INSTRUMENT_REGISTRY.CODE.eq(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE))
                .join(baseCurrencyRef)
                .on(baseCurrencyRef.CODE.eq(INSTRUMENT_FX_SPOT.BASE_CURRENCY))
                .join(quoteCurrencyRef)
                .on(quoteCurrencyRef.CODE.eq(INSTRUMENT_FX_SPOT.QUOTE_CURRENCY))
                .orderBy(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE.asc())
                .fetch(record -> toDomain(record, baseCurrencyRef, quoteCurrencyRef));
    }

    /* Проверяет наличие валюты в reference-таблице currency. */
    private void ensureCurrencyExists(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        boolean exists = dsl.fetchExists(
                dsl.selectOne()
                        .from(CURRENCY)
                        .where(CURRENCY.CODE.eq(currencyCode.value()))
        );

        if (!exists) {
            throw new CurrencyNotFoundException(currencyCode);
        }
    }

    /* Находит FOREX_SPOT по коду инструмента с join на instrument_registry и currency aliases. */
    private Optional<FxSpot> findByCode(DSLContext context, InstrumentCode instrumentCode) {
        var baseCurrencyRef = CURRENCY.as("base_currency_ref");
        var quoteCurrencyRef = CURRENCY.as("quote_currency_ref");

        // Ищем один FX_SPOT по коду и маппим в доменную модель.
        return context.select(fxSpotSelectFields(baseCurrencyRef, quoteCurrencyRef))
                .from(INSTRUMENT_FX_SPOT)
                .join(INSTRUMENT_REGISTRY)
                .on(INSTRUMENT_REGISTRY.CODE.eq(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE))
                .join(baseCurrencyRef)
                .on(baseCurrencyRef.CODE.eq(INSTRUMENT_FX_SPOT.BASE_CURRENCY))
                .join(quoteCurrencyRef)
                .on(quoteCurrencyRef.CODE.eq(INSTRUMENT_FX_SPOT.QUOTE_CURRENCY))
                .where(INSTRUMENT_FX_SPOT.INSTRUMENT_CODE.eq(instrumentCode.value()))
                .fetchOptional(record -> toDomain(record, baseCurrencyRef, quoteCurrencyRef));
    }

    /* Набор полей для выборки FX_SPOT вместе с алиасами базовой и котируемой валют. */
    private SelectFieldOrAsterisk[] fxSpotSelectFields(
            com.alligator.market.backend.infra.jooq.generated.tables.Currency baseCurrencyRef,
            com.alligator.market.backend.infra.jooq.generated.tables.Currency quoteCurrencyRef
    ) {
        return new SelectFieldOrAsterisk[]{
                INSTRUMENT_FX_SPOT.INSTRUMENT_CODE,
                INSTRUMENT_FX_SPOT.BASE_CURRENCY,
                INSTRUMENT_FX_SPOT.QUOTE_CURRENCY,
                INSTRUMENT_FX_SPOT.TENOR,
                INSTRUMENT_FX_SPOT.QUOTE_FRACTION_DIGITS,
                baseCurrencyRef.CODE,
                baseCurrencyRef.NAME,
                baseCurrencyRef.COUNTRY,
                baseCurrencyRef.FRACTION_DIGITS,
                quoteCurrencyRef.CODE,
                quoteCurrencyRef.NAME,
                quoteCurrencyRef.COUNTRY,
                quoteCurrencyRef.FRACTION_DIGITS
        };
    }

    /* Маппинг записи jOOQ в доменную модель FxSpot. */
    private FxSpot toDomain(
            Record record,
            com.alligator.market.backend.infra.jooq.generated.tables.Currency baseCurrencyRef,
            com.alligator.market.backend.infra.jooq.generated.tables.Currency quoteCurrencyRef
    ) {
        Objects.requireNonNull(record, "record must not be null");

        // Собираем валюты и параметры инструмента из записи.
        Currency base = toCurrency(record, baseCurrencyRef);
        Currency quote = toCurrency(record, quoteCurrencyRef);
        FxSpotTenor tenor = FxSpotTenor.valueOf(record.get(INSTRUMENT_FX_SPOT.TENOR));
        Integer quoteFractionDigits = record.get(INSTRUMENT_FX_SPOT.QUOTE_FRACTION_DIGITS);

        return new FxSpot(base, quote, tenor, quoteFractionDigits);
    }

    /* Сборка доменной валюты из aliased currency-записи join-результата. */
    private Currency toCurrency(
            Record record,
            com.alligator.market.backend.infra.jooq.generated.tables.Currency currencyRef
    ) {
        Objects.requireNonNull(record, "record must not be null");
        Objects.requireNonNull(currencyRef, "currencyRef must not be null");

        return new Currency(
                CurrencyCode.of(record.get(currencyRef.CODE)),
                record.get(currencyRef.NAME),
                record.get(currencyRef.COUNTRY),
                record.get(currencyRef.FRACTION_DIGITS)
        );
    }
}
