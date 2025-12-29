package com.alligator.market.backend.common.persistence.jpa.converter;

import jakarta.persistence.Converter;

/**
 * JPA-конвертер {@link java.time.Duration} ↔ {@link Long} (секунды) для хранения в БД.
 */
@Converter
public class DurationToSecondsConverter
        implements jakarta.persistence.AttributeConverter<java.time.Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(java.time.Duration attribute) {
        return attribute == null ? null : attribute.toSeconds();
    }

    @Override
    public java.time.Duration convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : java.time.Duration.ofSeconds(dbData);
    }
}
