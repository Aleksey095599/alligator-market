package com.alligator.market.backend.instrument.api.query.catalog.dto;

import java.util.List;

public record InstrumentCatalogItemResponse(
        String instrumentCode,
        String displayName,
        String asset,
        String product,
        String description,
        List<InstrumentAttributeResponse> attributes
) {
}
