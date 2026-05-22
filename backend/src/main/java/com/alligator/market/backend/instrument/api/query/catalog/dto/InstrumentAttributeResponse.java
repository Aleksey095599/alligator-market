package com.alligator.market.backend.instrument.api.query.catalog.dto;

public record InstrumentAttributeResponse(
        String key,
        String label,
        String value
) {
}
