package com.alligator.market.domain.marketdata.provider.model.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.marketdata.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Список поддерживаемых инструментов пуст.
 */
public final class SupportedInstrumentCodesEmptyException extends BaseDomainException {

    private final HandlerCode handlerCode;

    /**
     * Создает исключение.
     *
     * @param handlerCode код обработчика
     */
    public SupportedInstrumentCodesEmptyException(HandlerCode handlerCode) {
        super(DomainErrorCode.SUPPORTED_INSTRUMENT_CODES_EMPTY, msg(handlerCode));
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param handlerCode код обработчика
     * @param cause       причина ошибки
     */
    @SuppressWarnings("unused")
    public SupportedInstrumentCodesEmptyException(HandlerCode handlerCode, Throwable cause) {
        super(DomainErrorCode.SUPPORTED_INSTRUMENT_CODES_EMPTY, msg(handlerCode), cause);
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     */
    private static String msg(HandlerCode handlerCode) {
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        return "Supported instrument codes must not be empty (handlerCode=" + hc.value() + ")";
    }

    /**
     * Возвращает код обработчика.
     */
    @SuppressWarnings("unused")
    public HandlerCode getHandlerCode() {
        return handlerCode;
    }
}
