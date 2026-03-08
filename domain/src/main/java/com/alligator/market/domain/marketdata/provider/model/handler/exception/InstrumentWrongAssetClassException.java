package com.alligator.market.domain.marketdata.provider.model.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.base.model.classification.AssetClass;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Класс актива инструмента не соответствует обработчику.
 */
public final class InstrumentWrongAssetClassException extends BaseDomainException {

    private final InstrumentCode instrumentCode;
    private final AssetClass assetClass;
    private final HandlerCode handlerCode;
    private final AssetClass expectedAssetClass;

    /**
     * Создает исключение.
     */
    public InstrumentWrongAssetClassException(
            InstrumentCode instrumentCode,
            AssetClass assetClass,
            HandlerCode handlerCode,
            AssetClass expectedAssetClass
    ) {
        super(
                DomainErrorCode.INSTRUMENT_WRONG_ASSET_CLASS,
                msg(instrumentCode, assetClass, handlerCode, expectedAssetClass)
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.assetClass = Objects.requireNonNull(assetClass, "assetClass must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.expectedAssetClass = Objects.requireNonNull(expectedAssetClass, "expectedAssetClass must not be null");
    }

    private static String msg(
            InstrumentCode instrumentCode,
            AssetClass assetClass,
            HandlerCode handlerCode,
            AssetClass expectedAssetClass
    ) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        AssetClass actual = Objects.requireNonNull(assetClass, "assetClass must not be null");
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        AssetClass expected = Objects.requireNonNull(expectedAssetClass, "expectedAssetClass must not be null");
        return "Instrument asset class mismatch (instrumentCode=" + ic.value() + ", actualAssetClass=" + actual.name()
                + ", handlerCode=" + hc.value() + ", expectedAssetClass=" + expected.name() + ")";
    }

    public InstrumentCode getInstrumentCode() {
        return instrumentCode;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    public HandlerCode getHandlerCode() {
        return handlerCode;
    }

    public AssetClass getExpectedAssetClass() {
        return expectedAssetClass;
    }
}
