package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.dto;

public record CurrencyListItemResponse(
        String code,
        String name,
        String country,
        Integer fractionDigits
) {
}
