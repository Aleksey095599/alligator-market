package com.alligator.market.backend.instrument.application.catalog.exception;

import com.alligator.market.domain.instrument.Instrument;

import java.util.Objects;

public final class InstrumentCatalogDescriptorNotFoundException extends IllegalStateException {

    public InstrumentCatalogDescriptorNotFoundException(Instrument instrument) {
        super(message(instrument));
    }

    private static String message(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        return "Instrument catalog descriptor is not configured for instrument "
                + "(instrumentCode=%s, class=%s)".formatted(
                instrument.instrumentCode().value(),
                instrument.getClass().getName()
        );
    }
}
