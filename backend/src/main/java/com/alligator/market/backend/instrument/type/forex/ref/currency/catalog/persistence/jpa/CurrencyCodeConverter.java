package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link CurrencyCode} в виде строкового значения в БД.
 */
@Converter(autoApply = true)
public class CurrencyCodeConverter implements AttributeConverter<CurrencyCode, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(CurrencyCode attribute) {
        return attribute != null ? attribute.value() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public CurrencyCode convertToEntityAttribute(String dbData) {
        return dbData != null ? CurrencyCode.of(dbData) : null;
    }
}
