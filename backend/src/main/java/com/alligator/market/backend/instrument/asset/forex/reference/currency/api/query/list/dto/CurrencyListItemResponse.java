package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.dto;

/**
 * Элемент списка валют для list query.
 */
public record CurrencyListItemResponse(
        String code,
        String name,
        String country,
        Integer fractionDigits
) {
}
