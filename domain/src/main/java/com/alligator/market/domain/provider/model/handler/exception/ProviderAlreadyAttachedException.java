package com.alligator.market.domain.provider.model.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Провайдер уже прикреплен к обработчику.
 */
public final class ProviderAlreadyAttachedException extends BaseDomainException {

    private final HandlerCode handlerCode;

    /**
     * Создает исключение.
     *
     * @param handlerCode код обработчика
     */
    public ProviderAlreadyAttachedException(HandlerCode handlerCode) {
        super(DomainErrorCode.PROVIDER_ALREADY_ATTACHED, msg(handlerCode));
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param handlerCode код обработчика
     * @param cause       причина ошибки
     */
    @SuppressWarnings("unused")
    public ProviderAlreadyAttachedException(HandlerCode handlerCode, Throwable cause) {
        super(DomainErrorCode.PROVIDER_ALREADY_ATTACHED, msg(handlerCode), cause);
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     */
    private static String msg(HandlerCode handlerCode) {
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        return "Provider is already attached (handlerCode=" + hc.value() + ")";
    }

    /**
     * Возвращает код обработчика.
     */
    @SuppressWarnings("unused")
    public HandlerCode getHandlerCode() {
        return handlerCode;
    }
}
