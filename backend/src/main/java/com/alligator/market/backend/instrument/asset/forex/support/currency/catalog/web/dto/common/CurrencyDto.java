package com.alligator.market.backend.instrument.asset.forex.support.currency.catalog.web.dto.common;

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
