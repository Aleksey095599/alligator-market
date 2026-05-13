package com.alligator.market.domain.instrument.asset.forex.fxspot.codec;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;

import java.util.Objects;

public final class FxSpotCodec {

    private static final String TYPE_PREFIX = "FOREX_SPOT_";

    private static final char SEP = '_';

    private FxSpotCodec() {
        throw new UnsupportedOperationException("Utility class instantiation is not allowed");
    }

    public static InstrumentSymbol fxSpotSymbol(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentSymbol.of(baseCode.value() + quoteCode.value() + SEP + tenor.code());
    }

    public static InstrumentCode fxSpotCode(CurrencyCode baseCode, CurrencyCode quoteCode, FxSpotTenor tenor) {
        Objects.requireNonNull(baseCode, "baseCode must not be null");
        Objects.requireNonNull(quoteCode, "quoteCode must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        return InstrumentCode.of(TYPE_PREFIX + fxSpotSymbol(baseCode, quoteCode, tenor).value());
    }
}
