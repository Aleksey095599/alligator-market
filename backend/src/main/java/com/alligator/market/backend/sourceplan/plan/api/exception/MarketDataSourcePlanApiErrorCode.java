package com.alligator.market.backend.sourceplan.plan.api.exception;

/**
 * Локальные API-коды ошибок sourcing/plan feature.
 */
public enum MarketDataSourcePlanApiErrorCode {

    CAPTURE_PROCESS_CODE_NOT_FOUND,
    INSTRUMENT_CODE_NOT_FOUND,
    MARKET_DATA_SOURCE_CODES_NOT_FOUND,
    MARKET_DATA_SOURCE_PLAN_ALREADY_EXISTS,
    MARKET_DATA_SOURCE_PLAN_NOT_FOUND;

    public String code() {
        return name();
    }
}
