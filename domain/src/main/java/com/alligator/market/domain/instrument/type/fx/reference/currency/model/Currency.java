package com.alligator.market.domain.instrument.type.fx.reference.currency.model;

/**
 * Модель валюты.
 */
public record Currency(

        String code,
        String name,
        String country,
        Integer decimal
) {}
