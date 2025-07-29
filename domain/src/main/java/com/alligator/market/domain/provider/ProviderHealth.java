package com.alligator.market.domain.provider;

public enum ProviderHealth {

    UNKNOWN, // В том числе если не реализовано
    UP,
    DOWN,
    DEGRADED // частичный сбой, высокий лаг и т.п.
}
