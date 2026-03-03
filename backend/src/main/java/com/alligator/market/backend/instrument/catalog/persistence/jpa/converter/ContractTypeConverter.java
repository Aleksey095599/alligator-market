package com.alligator.market.backend.instrument.catalog.persistence.jpa.converter;

import com.alligator.market.domain.instrument.type.ContractType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link ContractType} в виде строкового значения в БД.
 */
@Converter(autoApply = true)
public class ContractTypeConverter implements AttributeConverter<ContractType, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(ContractType attribute) {
        return attribute != null ? attribute.name() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public ContractType convertToEntityAttribute(String dbData) {
        return dbData != null ? ContractType.valueOf(dbData) : null;
    }
}
