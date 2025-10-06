package com.alligator.market.domain.instrument.type.forex.spot.codec;

import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;

/**
 * Составные части кода инструмента FX_SPOT.
 */
public record FxSpotCodeParts(String base, String quote, ValueDateCode valueDate) {
}
