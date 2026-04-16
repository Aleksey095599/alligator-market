package com.alligator.market.backend.instrument.base.catalog.persistence.jpa.converter;

import com.alligator.market.domain.instrument.base.vo.InstrumentSymbol;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link InstrumentSymbol} в виде строкового значения в БД.
 */
@Converter(autoApply = true)
public class InstrumentSymbolConverter implements AttributeConverter<InstrumentSymbol, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(InstrumentSymbol attribute) {
        return attribute != null ? attribute.value() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public InstrumentSymbol convertToEntityAttribute(String dbData) {
        return dbData != null ? InstrumentSymbol.of(dbData) : null;
    }
}
