package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common;

/**
 * Общий DTO валюты (используется как in/out).
 */
public record CurrencyDto(
        String code,
        String name,
        String country,
        Integer fractionDigits
) {
}
