package com.alligator.market.backend.instrument.application.catalog.model;

import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;
import java.util.Objects;

public record InstrumentCatalogItem(
        InstrumentCode instrumentCode,
        String displayName,
        Asset asset,
        Product product,
        String description,
        List<InstrumentAttribute> attributes
) {
    public InstrumentCatalogItem {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        displayName = requireText(displayName, "displayName");
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(product, "product must not be null");
        description = requireText(description, "description");
        attributes = List.copyOf(Objects.requireNonNull(attributes, "attributes must not be null"));
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return value;
    }
}
