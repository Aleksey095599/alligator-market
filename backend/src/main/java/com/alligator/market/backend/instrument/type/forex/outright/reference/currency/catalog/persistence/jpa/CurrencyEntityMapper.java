package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntityMappingConfig;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Маппер: модель валюты ⇄ сущность.
 */
@Mapper(componentModel = "spring", config = BaseEntityMappingConfig.class)
public interface CurrencyEntityMapper {

    /** Преобразует сущность в модель. */
    Currency toDomain(CurrencyEntity entity);

    /** Обновляет сущность данными модели. */
    @Mapping(target = "id", ignore = true)
    @InheritConfiguration(name = "ignoreBaseEntityFields")
    void updateEntity(Currency currency, @MappingTarget CurrencyEntity entity);
}
