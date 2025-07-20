package com.alligator.market.domain.instrument.type.forex.currency;

/**
 * Доменная модель валюты.
 */
public record Currency (

        String code,
        String name,
        String country,
        Integer decimal
) {}
