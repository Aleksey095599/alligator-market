package com.alligator.market.backend.provider.catalog.persistence.jpa.policy;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

/**
 * Конвертер длительности в секунды и обратно для хранения в БД.
 */
@Converter(autoApply = false)
public class DurationToSecondsConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        return Duration.ofSeconds(dbData);
    }
}
