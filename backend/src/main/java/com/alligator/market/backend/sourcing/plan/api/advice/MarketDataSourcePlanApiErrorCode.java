package com.alligator.market.backend.sourcing.plan.api.advice;

/**
 * Локальные API-коды ошибок sourcing/plan feature.
 */
public enum MarketDataSourcePlanApiErrorCode {

    INSTRUMENT_CODE_NOT_FOUND,
    PROVIDER_CODES_NOT_FOUND,
    MARKET_DATA_SOURCE_PLAN_ALREADY_EXISTS,
    MARKET_DATA_SOURCE_PLAN_NOT_FOUND;

    public String code() {
        return name();
    }
}
