package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Маппер: модель ⇄ сущность.
 */
@Mapper(componentModel = "spring")
public interface FxSpotEntityMapper {

    /** Преобразует сущность в доменную модель. */
    @Mapping(target = "baseCurrency", source = "entity.baseCurrency.code")
    @Mapping(target = "quoteCurrency", source = "entity.quoteCurrency.code")
    FxSpot toDomain(FxSpotEntity entity);

    /** Обновляет сущность данными из доменной модели и валют. */
    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @Mapping(target = "quoteCurrency", source = "quoteCurrency")
    @Mapping(target = "quoteDecimal", source = "model.quoteDecimal")
    @Mapping(target = "valueDateCode", source = "model.valueDateCode")
    @Mapping(target = "code", expression = "java(model.getCode())")
    @Mapping(target = "type", expression = "java(model.getType())")
    void updateEntity(FxSpot model,
                      CurrencyEntity baseCurrency,
                      CurrencyEntity quoteCurrency,
                      @MappingTarget FxSpotEntity entity);
}

