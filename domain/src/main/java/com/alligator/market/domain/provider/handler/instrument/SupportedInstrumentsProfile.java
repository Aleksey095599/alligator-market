package com.alligator.market.domain.provider.handler.instrument;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.classification.Asset;
import com.alligator.market.domain.instrument.classification.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

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
     * Канонический конструктор с fail-fast валидацией.
     */
    public SupportedInstrumentsProfile {
        Objects.requireNonNull(supportedInstrumentCodes, "instrumentCode must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(product, "product must not be null");

        // Создаём защитную копию набора
        supportedInstrumentCodes = Set.copyOf(supportedInstrumentCodes);
    }
}
