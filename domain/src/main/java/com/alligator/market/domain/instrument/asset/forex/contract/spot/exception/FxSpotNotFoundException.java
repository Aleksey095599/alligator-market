package com.alligator.market.domain.instrument.asset.forex.contract.spot.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка поиска инструмента FOREX_SPOT.
 */
public final class FxSpotNotFoundException extends BaseDomainException {

    private final InstrumentCode instrumentCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    public FxSpotNotFoundException(InstrumentCode instrumentCode) {
        super(DomainErrorCode.FX_SPOT_NOT_FOUND, msg(instrumentCode));
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public FxSpotNotFoundException(InstrumentCode instrumentCode, Throwable cause) {
        super(DomainErrorCode.FX_SPOT_NOT_FOUND, msg(instrumentCode), cause);
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(InstrumentCode instrumentCode) {
        InstrumentCode c = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "FX_SPOT instrument not found (code=" + c.value() + ")";
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
}
