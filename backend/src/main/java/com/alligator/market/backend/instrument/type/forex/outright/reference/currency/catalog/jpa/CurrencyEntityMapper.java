package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa;

import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Маппер между сущностью валюты и доменной моделью.
 */
@Mapper(componentModel = "spring")
public interface CurrencyEntityMapper {

    /** Преобразует сущность в доменную модель. */
    Currency toDomain(CurrencyEntity entity);

    /** Преобразует доменную модель в новую сущность. */
    CurrencyEntity toEntity(Currency currency);

    /** Обновляет сущность данными доменной модели. */
    void updateEntity(Currency currency, @MappingTarget CurrencyEntity entity);
}

