package com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.mapper;

import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Маппер: модель инструмента FX_OUTRIGHT ⇄ сущность инструмента FX_OUTRIGHT.
 */
@Mapper(componentModel = "spring")
public interface FxOutrightEntityMapper {

    /** Преобразует сущность в доменную модель. */
    FxOutright toDomain(FxOutrightEntity entity);

    /** Обновляет сущность данными из доменной модели и валют. */
    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @Mapping(target = "quoteCurrency", source = "quoteCurrency")
    @Mapping(target = "quoteDecimal", source = "model.quoteDecimal")
    @Mapping(target = "valueDateCode", source = "model.valueDateCode")
    void updateEntity(FxOutright model,
                      CurrencyEntity baseCurrency,
                      CurrencyEntity quoteCurrency,
                      @MappingTarget FxOutrightEntity entity);
}

