package com.alligator.market.backend.instrument.api.query.catalog.mapper;

import com.alligator.market.backend.instrument.api.query.catalog.dto.InstrumentAttributeResponse;
import com.alligator.market.backend.instrument.application.catalog.model.InstrumentAttribute;

import java.util.Objects;

public final class InstrumentAttributeResponseMapper {
    private InstrumentAttributeResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static InstrumentAttributeResponse toResponse(InstrumentAttribute attribute) {
        Objects.requireNonNull(attribute, "attribute must not be null");

        return new InstrumentAttributeResponse(
                attribute.key(),
                attribute.label(),
                attribute.value()
        );
    }
}
