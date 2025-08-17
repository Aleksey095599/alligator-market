package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.mapper;

import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Маппер: модель валюты ⇄ сущность валюты.
 */
@Mapper(componentModel = "spring")
public interface CurrencyEntityMapper {

    /** Преобразует сущность в модель. */
    Currency toDomain(CurrencyEntity entity);

    /** Обновляет сущность данными модели. */
    void updateEntity(Currency currency, @MappingTarget CurrencyEntity entity);
}
