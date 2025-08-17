package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.mapper;

import com.alligator.market.backend.common.jpa.BaseEntityMappingConfig;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Маппер: модель валюты ⇄ сущность валюты.
 */
@Mapper(componentModel = "spring", config = BaseEntityMappingConfig.class)
public interface CurrencyEntityMapper {

    /** Преобразует сущность в модель. */
    Currency toDomain(CurrencyEntity entity);

    /** Обновляет сущность данными модели. */
    @InheritConfiguration(name = "ignoreBaseEntityFields")
    void updateEntity(Currency currency, @MappingTarget CurrencyEntity entity);
}
