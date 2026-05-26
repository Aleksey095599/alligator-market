package com.alligator.market.domain.source.exception;

import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.HandlerCode;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InstrumentNotSupportedByHandlerException extends IllegalArgumentException {
    public enum Reason {
        INSTRUMENT_CLASS_MISMATCH,
        ASSET_MISMATCH,
        PRODUCT_MISMATCH,
        INSTRUMENT_CODE_NOT_SUPPORTED
    }

    private final InstrumentCode instrumentCode;
    private final HandlerCode handlerCode;
    private final Reason reason;
    private final String expectedValue;
    private final String actualValue;

    private InstrumentNotSupportedByHandlerException(
            InstrumentCode instrumentCode,
            HandlerCode handlerCode,
            Reason reason,
            String expectedValue,
            String actualValue
    ) {
        super(message(instrumentCode, handlerCode, reason, expectedValue, actualValue));
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.reason = Objects.requireNonNull(reason, "reason must not be null");
        this.expectedValue = Objects.requireNonNull(expectedValue, "expectedValue must not be null");
        this.actualValue = Objects.requireNonNull(actualValue, "actualValue must not be null");
    }

    public static InstrumentNotSupportedByHandlerException instrumentClassMismatch(
            InstrumentCode instrumentCode,
            HandlerCode handlerCode,
            Class<?> expectedInstrumentClass,
            Class<?> actualInstrumentClass
    ) {
        return new InstrumentNotSupportedByHandlerException(
                instrumentCode,
                handlerCode,
                Reason.INSTRUMENT_CLASS_MISMATCH,
                className(expectedInstrumentClass),
                className(actualInstrumentClass)
        );
    }

    public static InstrumentNotSupportedByHandlerException assetMismatch(
            InstrumentCode instrumentCode,
            HandlerCode handlerCode,
            Asset expectedAsset,
            Asset actualAsset
    ) {
        return new InstrumentNotSupportedByHandlerException(
                instrumentCode,
                handlerCode,
                Reason.ASSET_MISMATCH,
                assetCode(expectedAsset),
                assetCode(actualAsset)
        );
    }

    public static InstrumentNotSupportedByHandlerException productMismatch(
            InstrumentCode instrumentCode,
            HandlerCode handlerCode,
            Product expectedProduct,
            Product actualProduct
    ) {
        return new InstrumentNotSupportedByHandlerException(
                instrumentCode,
                handlerCode,
                Reason.PRODUCT_MISMATCH,
                productCode(expectedProduct),
                productCode(actualProduct)
        );
    }

    public static InstrumentNotSupportedByHandlerException instrumentCodeNotSupported(
            InstrumentCode instrumentCode,
            HandlerCode handlerCode,
            Collection<InstrumentCode> supportedInstrumentCodes
    ) {
        return new InstrumentNotSupportedByHandlerException(
                instrumentCode,
                handlerCode,
                Reason.INSTRUMENT_CODE_NOT_SUPPORTED,
                formatInstrumentCodes(supportedInstrumentCodes),
                instrumentCode.value()
        );
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public HandlerCode handlerCode() {
        return handlerCode;
    }

    public Reason reason() {
        return reason;
    }

    public String expectedValue() {
        return expectedValue;
    }

    public String actualValue() {
        return actualValue;
    }

    private static String message(
            InstrumentCode instrumentCode,
            HandlerCode handlerCode,
            Reason reason,
            String expectedValue,
            String actualValue
    ) {
        return ("Instrument '%s' is not supported by handler '%s' " +
                "(reason=%s, expected=%s, actual=%s)").formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value(),
                Objects.requireNonNull(handlerCode, "handlerCode must not be null").value(),
                Objects.requireNonNull(reason, "reason must not be null"),
                Objects.requireNonNull(expectedValue, "expectedValue must not be null"),
                Objects.requireNonNull(actualValue, "actualValue must not be null")
        );
    }

    private static String className(Class<?> instrumentClass) {
        return Objects.requireNonNull(instrumentClass, "instrumentClass must not be null").getName();
    }

    private static String assetCode(Asset asset) {
        return asset == null ? "<null>" : asset.code();
    }

    private static String productCode(Product product) {
        return product == null ? "<null>" : product.code();
    }

    private static String formatInstrumentCodes(Collection<InstrumentCode> instrumentCodes) {
        Objects.requireNonNull(instrumentCodes, "instrumentCodes must not be null");

        return instrumentCodes.stream()
                .map(instrumentCode -> Objects.requireNonNull(
                        instrumentCode,
                        "instrumentCode must not be null"
                ))
                .map(InstrumentCode::value)
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
