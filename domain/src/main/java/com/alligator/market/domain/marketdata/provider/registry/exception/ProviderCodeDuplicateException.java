package com.alligator.market.domain.marketdata.provider.registry.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;

import java.util.Objects;

/**
 * Дублирование кодов провайдеров.
 */
public final class ProviderCodeDuplicateException extends BaseDomainException {

    private final ProviderCode providerCode;

    /**
     * Создает исключение.
     *
     * @param providerCode код провайдера
     */
    public ProviderCodeDuplicateException(ProviderCode providerCode) {
        super(DomainErrorCode.PROVIDER_CODE_DUPLICATE, msg(providerCode));
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param providerCode код провайдера
     * @param cause        причина ошибки
     */
    @SuppressWarnings("unused")
    public ProviderCodeDuplicateException(ProviderCode providerCode, Throwable cause) {
        super(DomainErrorCode.PROVIDER_CODE_DUPLICATE, msg(providerCode), cause);
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param providerCode код провайдера
     * @return текст сообщения
     */
    private static String msg(ProviderCode providerCode) {
        ProviderCode pc = Objects.requireNonNull(providerCode, "providerCode must not be null");
        return "Duplicate provider code detected (code=" + pc.value() + ")";
    }

    /**
     * Возвращает код провайдера.
     *
     * @return код провайдера
     */
    @SuppressWarnings("unused")
    public ProviderCode getProviderCode() {
        return providerCode;
    }
}
