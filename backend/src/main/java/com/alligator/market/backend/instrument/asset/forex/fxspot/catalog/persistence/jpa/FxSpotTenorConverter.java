package com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.asset.forex.spot.model.FxSpotTenor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для хранения {@link FxSpotTenor} в виде строкового значения в БД.
 */
@Converter
public class FxSpotTenorConverter implements AttributeConverter<FxSpotTenor, String> {

    /**
     * Преобразует доменную модель в значение для БД.
     */
    @Override
    public String convertToDatabaseColumn(FxSpotTenor attribute) {
        return attribute != null ? attribute.name() : null;
    }

    /**
     * Преобразует значение из БД в доменную модель.
     */
    @Override
    public FxSpotTenor convertToEntityAttribute(String dbData) {
        return dbData != null ? FxSpotTenor.valueOf(dbData) : null;
    }
}
