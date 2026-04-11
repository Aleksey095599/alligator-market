package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.in;

/**
 * DTO обновления валюты (in).
 */
public record CurrencyUpdateDto(
        String name,
        String country,
        Integer fractionDigits
) {
}
