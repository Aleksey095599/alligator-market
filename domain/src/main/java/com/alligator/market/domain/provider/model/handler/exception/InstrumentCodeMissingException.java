package com.alligator.market.domain.provider.model.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Код инструмента отсутствует.
 */
public final class InstrumentCodeMissingException extends BaseDomainException {

    private final HandlerCode handlerCode;

    /**
     * Создает исключение.
     *
     * @param handlerCode код обработчика
     */
    public InstrumentCodeMissingException(HandlerCode handlerCode) {
        super(DomainErrorCode.INSTRUMENT_CODE_MISSING, msg(handlerCode));
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param handlerCode код обработчика
     * @param cause       причина ошибки
     */
    @SuppressWarnings("unused")
    public InstrumentCodeMissingException(HandlerCode handlerCode, Throwable cause) {
        super(DomainErrorCode.INSTRUMENT_CODE_MISSING, msg(handlerCode), cause);
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     */
    private static String msg(HandlerCode handlerCode) {
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        return "Instrument code must not be null (handlerCode=" + hc.value() + ")";
    }

    /**
     * Возвращает код обработчика.
     */
    @SuppressWarnings("unused")
    public HandlerCode getHandlerCode() {
        return handlerCode;
    }
}
