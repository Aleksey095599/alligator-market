package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import com.alligator.market.domain.provider.model.vo.ProviderCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link ProviderCode} в виде строкового значения в БД.
 */
@Converter(autoApply = true)
public class ProviderCodeConverter implements AttributeConverter<ProviderCode, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(ProviderCode attribute) {
        return attribute != null ? attribute.value() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public ProviderCode convertToEntityAttribute(String dbData) {
        return dbData != null ? ProviderCode.of(dbData) : null;
    }
}
