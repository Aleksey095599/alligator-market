package com.alligator.market.domain.source.handler.instrument;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.classification.Asset;
import com.alligator.market.domain.instrument.classification.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.HandlerCode;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Сводный профиль инструментов, поддерживаемых обработчиком.
 *
 * <p>Назначение: Фиксирует набор кодов инструментов и параметры, которым должен соответствовать инструмент,
 * чтобы быть совместимым с обработчиком.</p>
 *
 * @param supportedInstrumentCodes Набор допустимых кодов инструментов
 * @param instrumentClass          Класс инструмента
 * @param asset                    Актив, лежащий в основе финансового инструмента
 * @param product                  Продукт (тип контракта) финансового инструмента
 */
public record SupportedInstrumentsProfile(
        Set<InstrumentCode> supportedInstrumentCodes,
        Class<? extends Instrument> instrumentClass,
        Asset asset,
        Product product
) {

    /**
     * Строит сводный профиль из supportedInstruments с полной fail-fast валидацией.
     */
    public static <I extends Instrument> SupportedInstrumentsProfile fromSupportedInstruments(
            HandlerCode handlerCode,
            Class<I> instrumentClass,
            Set<? extends I> supportedInstruments
    ) {
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(supportedInstruments, "supportedInstruments must not be null");

        if (supportedInstruments.isEmpty()) {
            throw new IllegalArgumentException("supportedInstruments must not be empty");
        }

        Asset resolvedAsset = null;
        Product resolvedProduct = null;
        LinkedHashSet<InstrumentCode> resolvedCodes = new LinkedHashSet<>();

        for (I instrument : supportedInstruments) {
            Objects.requireNonNull(instrument, "supportedInstrument must not be null");

            InstrumentCode instrumentCode = Objects.requireNonNull(
                    instrument.instrumentCode(),
                    "supported instrumentCode must not be null"
            );

            if (!instrumentClass.isInstance(instrument)) {
                throw new IllegalArgumentException(
                        "Supported instrument '%s' has java class '%s', but handler '%s' expects '%s'"
                                .formatted(
                                        instrumentCode.value(),
                                        instrument.getClass().getName(),
                                        handlerCode.value(),
                                        instrumentClass.getName()
                                )
                );
            }

            Asset currentAsset = Objects.requireNonNull(instrument.asset(), "supported asset must not be null");
            Product currentProduct = Objects.requireNonNull(instrument.product(), "supported product must not be null");

            if (resolvedAsset == null) {
                resolvedAsset = currentAsset;
                resolvedProduct = currentProduct;
            } else {
                if (currentAsset != resolvedAsset) {
                    throw new IllegalArgumentException(
                            "Supported instruments of handler '%s' have inconsistent asset: expected '%s', but found '%s' for instrument '%s'"
                                    .formatted(
                                            handlerCode.value(),
                                            resolvedAsset,
                                            currentAsset,
                                            instrumentCode.value()
                                    )
                    );
                }

                if (currentProduct != resolvedProduct) {
                    throw new IllegalArgumentException(
                            "Supported instruments of handler '%s' have inconsistent product: expected '%s', but found '%s' for instrument '%s'"
                                    .formatted(
                                            handlerCode.value(),
                                            resolvedProduct,
                                            currentProduct,
                                            instrumentCode.value()
                                    )
                    );
                }
            }

            if (!resolvedCodes.add(instrumentCode)) {
                throw new IllegalArgumentException(
                        "Supported instrument code '%s' is duplicated in handler '%s'"
                                .formatted(instrumentCode.value(), handlerCode.value())
                );
            }
        }

        return new SupportedInstrumentsProfile(
                freezeSupportedInstrumentCodes(resolvedCodes),
                instrumentClass,
                resolvedAsset,
                resolvedProduct
        );
    }

    /**
     * Канонический конструктор с fail-fast валидацией.
     */
    public SupportedInstrumentsProfile {
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(product, "product must not be null");

        // Создаём защитную копию набора
        supportedInstrumentCodes = Set.copyOf(supportedInstrumentCodes);
    }

    /*
     * Возвращает неизменяемую защищенную копию списка поддерживаемых инструментов.
     */
    private static Set<InstrumentCode> freezeSupportedInstrumentCodes(Set<InstrumentCode> supportedInstrumentCodes) {
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        LinkedHashSet<InstrumentCode> copy = new LinkedHashSet<>(supportedInstrumentCodes.size());
        for (InstrumentCode code : supportedInstrumentCodes) {
            Objects.requireNonNull(code, "code must not be null");
            copy.add(code);
        }

        return Collections.unmodifiableSet(copy);
    }
}
