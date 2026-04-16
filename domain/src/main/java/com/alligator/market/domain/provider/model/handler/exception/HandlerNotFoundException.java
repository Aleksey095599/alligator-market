package com.alligator.market.domain.provider.model.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Objects;

/**
 * Обработчик для инструмента не найден.
 */
public final class HandlerNotFoundException extends BaseDomainException {

    private final InstrumentCode instrumentCode;
    private final ProviderCode providerCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     * @param providerCode   код провайдера
     */
    public HandlerNotFoundException(InstrumentCode instrumentCode, ProviderCode providerCode) {
        super(DomainErrorCode.HANDLER_NOT_FOUND, msg(instrumentCode, providerCode));
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param providerCode   код провайдера
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public HandlerNotFoundException(InstrumentCode instrumentCode, ProviderCode providerCode, Throwable cause) {
        super(DomainErrorCode.HANDLER_NOT_FOUND, msg(instrumentCode, providerCode), cause);
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @param providerCode   код провайдера
     * @return текст сообщения
     */
    private static String msg(InstrumentCode instrumentCode, ProviderCode providerCode) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        ProviderCode pc = Objects.requireNonNull(providerCode, "providerCode must not be null");
        return "Handler not found (instrumentCode=" + ic.value() + ", providerCode=" + pc.value() + ")";
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    @SuppressWarnings("unused")
    public InstrumentCode getInstrumentCode() {
        return instrumentCode;
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
