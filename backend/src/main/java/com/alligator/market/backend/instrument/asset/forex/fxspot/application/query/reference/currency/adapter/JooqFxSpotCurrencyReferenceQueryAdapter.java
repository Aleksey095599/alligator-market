package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.adapter;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port.FxSpotCurrencyReferenceQueryPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentFxSpot.INSTRUMENT_FX_SPOT;

/**
 * jOOQ-адаптер query-port проверки ссылок FX_SPOT на валюту.
 */
public final class JooqFxSpotCurrencyReferenceQueryAdapter implements FxSpotCurrencyReferenceQueryPort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqFxSpotCurrencyReferenceQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean referencesCurrency(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return dsl.fetchExists(
                dsl.selectOne()
                        .from(INSTRUMENT_FX_SPOT)
                        .where(INSTRUMENT_FX_SPOT.BASE_CURRENCY.eq(currencyCode.value())
                                .or(INSTRUMENT_FX_SPOT.QUOTE_CURRENCY.eq(currencyCode.value())))
        );
    }
}
