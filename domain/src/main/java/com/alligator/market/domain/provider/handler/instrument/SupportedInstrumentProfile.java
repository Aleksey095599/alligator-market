package com.alligator.market.domain.provider.handler.instrument;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.classification.Asset;
import com.alligator.market.domain.instrument.classification.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Профиль поддерживаемого инструмента для конфигурации обработчика.
 */
public record SupportedInstrumentProfile(
        InstrumentCode instrumentCode,
        Class<? extends Instrument> instrumentJavaClass,
        Asset asset,
        Product product
) {

    /**
     * Канонический конструктор с fail-fast валидацией.
     */
    public SupportedInstrumentProfile {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(instrumentJavaClass, "instrumentJavaClass must not be null");
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(product, "product must not be null");
    }

    /**
     * Фабрика профиля из доменной модели инструмента.
     */
    public static SupportedInstrumentProfile from(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        return new SupportedInstrumentProfile(
                Objects.requireNonNull(instrument.instrumentCode(), "instrumentCode must not be null"),
                instrument.getClass(),
                Objects.requireNonNull(instrument.asset(), "asset must not be null"),
                Objects.requireNonNull(instrument.product(), "product must not be null")
        );
    }
}
