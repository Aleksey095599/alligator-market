package com.alligator.market.backend.provider.api.exception;

/**
 * Локальные API-коды ошибок provider feature.
 */
public enum ProviderApiErrorCode {

    PROVIDER_HANDLER_NOT_FOUND;

    public String code() {
        return name();
    }
}
