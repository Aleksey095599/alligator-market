package com.alligator.market.backend.instrument.catalog.persistence.jpa.converter;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link InstrumentCode} в виде строкового значения в БД.
 */
@Converter(autoApply = true)
public class InstrumentCodeConverter implements AttributeConverter<InstrumentCode, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(InstrumentCode attribute) {
        return attribute != null ? attribute.value() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public InstrumentCode convertToEntityAttribute(String dbData) {
        return dbData != null ? InstrumentCode.of(dbData) : null;
    }
}
