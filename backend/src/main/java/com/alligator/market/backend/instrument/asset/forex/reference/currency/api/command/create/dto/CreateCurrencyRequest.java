package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.dto;

/**
 * DTO запроса на создание валюты.
 */
public record CreateCurrencyRequest(
        String code,
        String name,
        String country,
        Integer fractionDigits
) {
}
