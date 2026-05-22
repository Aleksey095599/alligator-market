package com.alligator.market.backend.instrument.api.query.catalog.mapper;

import com.alligator.market.backend.instrument.api.query.catalog.dto.InstrumentCatalogItemResponse;
import com.alligator.market.backend.instrument.application.catalog.model.InstrumentCatalogItem;

import java.util.Objects;

public final class InstrumentCatalogItemResponseMapper {
    private InstrumentCatalogItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static InstrumentCatalogItemResponse toResponse(InstrumentCatalogItem item) {
        Objects.requireNonNull(item, "item must not be null");

        return new InstrumentCatalogItemResponse(
                item.instrumentCode().value(),
                item.displayName(),
                item.asset().code(),
                item.product().code(),
                item.description(),
                item.attributes()
                        .stream()
                        .map(InstrumentAttributeResponseMapper::toResponse)
                        .toList()
        );
    }
}
