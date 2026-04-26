package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.adapter;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.port.FxSpotCurrencyUsageQueryPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentFxSpot.INSTRUMENT_FX_SPOT;

/**
 * jOOQ-адаптер query-port проверки использования валюты в FOREX_SPOT.
 */
public final class JooqFxSpotCurrencyUsageQueryAdapter implements FxSpotCurrencyUsageQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqFxSpotCurrencyUsageQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCurrencyCode(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return dsl.fetchExists(
                dsl.selectOne()
                        .from(INSTRUMENT_FX_SPOT)
                        .where(INSTRUMENT_FX_SPOT.BASE_CURRENCY.eq(currencyCode.value())
                                .or(INSTRUMENT_FX_SPOT.QUOTE_CURRENCY.eq(currencyCode.value())))
        );
    }
}
