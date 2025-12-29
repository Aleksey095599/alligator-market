package com.alligator.market.backend.common.persistence.jpa.converter;

import jakarta.persistence.Converter;

import java.time.Duration;

/**
 * JPA-конвертер {@link java.time.Duration} ↔ {@link Long} (миллисекунды) для хранения в БД.
 *
 * <p>Точность — до миллисекунды: наносекунды отбрасываются (используется {@link Duration#toMillis()}),
 * {@code null} ↔ {@code null}.</p>
 */
@Converter
public class DurationToMillisConverter
        implements jakarta.persistence.AttributeConverter<java.time.Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(java.time.Duration attribute) {
        return attribute == null ? null : attribute.toMillis();
    }

    @Override
    public java.time.Duration convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : java.time.Duration.ofMillis(dbData);
    }
}
