package com.alligator.market.domain.instrument.type.forex.ref.currency.model;

/**
 * Модель валюты.
 */
public record Currency(

        String code,
        String name,
        String country,
        Integer decimal
) {}
