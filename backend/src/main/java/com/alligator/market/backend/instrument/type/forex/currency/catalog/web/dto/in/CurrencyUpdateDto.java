package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in;

/**
 * DTO обновления валюты (in).
 */
public record CurrencyUpdateDto(
        String name,
        String country,
        Integer fractionDigits
) {
}
