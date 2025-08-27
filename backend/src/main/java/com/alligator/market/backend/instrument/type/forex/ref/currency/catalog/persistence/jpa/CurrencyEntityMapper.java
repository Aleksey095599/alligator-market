package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import org.springframework.stereotype.Component;

/**
 * Маппер: модель валюты ⇄ сущность.
 */
@Component
public class CurrencyEntityMapper {

    /** Преобразует сущность в модель. */
    public Currency toDomain(CurrencyEntity entity) {
        // Собираем доменную модель
        return new Currency(
                entity.getCode(),
                entity.getName(),
                entity.getCountry(),
                entity.getDecimal()
        );
    }

    /** Обновляет сущность данными модели. */
    public void updateEntity(Currency currency, CurrencyEntity entity) {
        // Переносим значения в JPA-сущность
        entity.setCode(currency.code());
        entity.setName(currency.name());
        entity.setCountry(currency.country());
        entity.setDecimal(currency.decimal());
    }
}
