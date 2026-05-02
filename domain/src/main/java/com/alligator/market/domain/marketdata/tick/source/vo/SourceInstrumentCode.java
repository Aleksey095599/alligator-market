package com.alligator.market.domain.marketdata.tick.source.vo;

import java.util.Objects;

public record SourceInstrumentCode(String value) {

    public SourceInstrumentCode {
        Objects.requireNonNull(value, "value must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    public static SourceInstrumentCode of(String value) {
        return new SourceInstrumentCode(value);
    }
}
