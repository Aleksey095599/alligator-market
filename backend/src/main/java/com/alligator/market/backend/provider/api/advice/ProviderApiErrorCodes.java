package com.alligator.market.backend.provider.api.advice;

/**
 * Локальные API-коды ошибок provider feature.
 */
public enum ProviderApiErrorCodes {

    PROVIDER_HANDLER_NOT_FOUND;

    public String code() {
        return name();
    }
}
