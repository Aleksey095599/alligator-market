package com.alligator.market.domain.provider.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.base.classification.ContractType;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.provider.vo.HandlerCode;

import java.util.Objects;

/**
 * Тип контракта инструмента не соответствует обработчику.
 */
public final class InstrumentWrongContractTypeException extends BaseDomainException {

    private final InstrumentCode instrumentCode;
    private final ContractType contractType;
    private final HandlerCode handlerCode;
    private final ContractType expectedContractType;

    /**
     * Создает исключение.
     */
    public InstrumentWrongContractTypeException(
            InstrumentCode instrumentCode,
            ContractType contractType,
            HandlerCode handlerCode,
            ContractType expectedContractType
    ) {
        super(
                DomainErrorCode.INSTRUMENT_WRONG_CONTRACT_TYPE,
                msg(instrumentCode, contractType, handlerCode, expectedContractType)
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.contractType = Objects.requireNonNull(contractType, "contractType must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.expectedContractType = Objects.requireNonNull(expectedContractType, "expectedContractType must not be null");
    }

    private static String msg(
            InstrumentCode instrumentCode,
            ContractType contractType,
            HandlerCode handlerCode,
            ContractType expectedContractType
    ) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        ContractType actual = Objects.requireNonNull(contractType, "contractType must not be null");
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        ContractType expected = Objects.requireNonNull(expectedContractType, "expectedContractType must not be null");
        return "Instrument contract type mismatch (instrumentCode=" + ic.value() + ", actualContractType=" + actual.name()
                + ", handlerCode=" + hc.value() + ", expectedContractType=" + expected.name() + ")";
    }

    public InstrumentCode getInstrumentCode() {
        return instrumentCode;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public HandlerCode getHandlerCode() {
        return handlerCode;
    }

    public ContractType getExpectedContractType() {
        return expectedContractType;
    }
}
