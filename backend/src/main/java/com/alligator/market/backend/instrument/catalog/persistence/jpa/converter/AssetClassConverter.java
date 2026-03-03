package com.alligator.market.backend.instrument.catalog.persistence.jpa.converter;

import com.alligator.market.domain.instrument.type.AssetClass;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link AssetClass} в виде строкового значения в БД.
 */
@Converter(autoApply = true)
public class AssetClassConverter implements AttributeConverter<AssetClass, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(AssetClass attribute) {
        return attribute != null ? attribute.name() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public AssetClass convertToEntityAttribute(String dbData) {
        return dbData != null ? AssetClass.valueOf(dbData) : null;
    }
}
