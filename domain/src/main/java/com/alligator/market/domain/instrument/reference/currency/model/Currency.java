package com.alligator.market.domain.instrument.reference.currency.model;

/**
 * Модель валюты.
 */
public record Currency(

        String code,
        String name,
        String country,
        Integer decimal
) {}
