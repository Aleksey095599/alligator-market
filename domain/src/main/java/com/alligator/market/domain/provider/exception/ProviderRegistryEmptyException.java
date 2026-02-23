package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;

/**
 * Пустой реестр провайдеров.
 */
public final class ProviderRegistryEmptyException extends BaseDomainException {

    /**
     * Создает исключение.
     */
    public ProviderRegistryEmptyException() {
        super(DomainErrorCode.PROVIDER_REGISTRY_EMPTY, "Provider registry must contain at least one provider");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public ProviderRegistryEmptyException(Throwable cause) {
        super(DomainErrorCode.PROVIDER_REGISTRY_EMPTY,
                "Provider registry must contain at least one provider",
                cause);
    }
}
