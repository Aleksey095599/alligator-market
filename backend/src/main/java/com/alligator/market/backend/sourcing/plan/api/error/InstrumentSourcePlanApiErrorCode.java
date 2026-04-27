package com.alligator.market.backend.sourcing.plan.api.error;

/**
 * Локальные API-коды ошибок sourcing/plan feature.
 */
public enum InstrumentSourcePlanApiErrorCode {

    INSTRUMENT_CODE_NOT_FOUND,
    PROVIDER_CODES_NOT_FOUND,
    INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS,
    INSTRUMENT_SOURCE_PLAN_NOT_FOUND;

    public String code() {
        return name();
    }
}
