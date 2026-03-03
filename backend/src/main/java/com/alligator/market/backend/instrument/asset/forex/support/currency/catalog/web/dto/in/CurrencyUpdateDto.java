package com.alligator.market.backend.instrument.asset.forex.support.currency.catalog.web.dto.in;

/**
 * DTO обновления валюты (in).
 */
public record CurrencyUpdateDto(
        String name,
        String country,
        Integer fractionDigits
) {
}
