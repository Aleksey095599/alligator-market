package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import org.springframework.stereotype.Component;

/**
 * Маппер: JPA-сущность ⇄ доменная модель.
 */
@Component
public class CurrencyEntityMapper {

    /** JPA-сущность ⇒ доменная модель. */
    public Currency toDomain(CurrencyEntity entity) {
        // Собираем доменную модель
        return new Currency(
                CurrencyCode.of(entity.getCode()),
                entity.getName(),
                entity.getCountry(),
                entity.getDecimal()
        );
    }

    /** Обновление JPA-сущности. */
    public void updateEntity(Currency currency, CurrencyEntity entity) {
        // Переносим значения в JPA-сущность
        entity.setCode(currency.code().value());
        entity.setName(currency.name());
        entity.setCountry(currency.country());
        entity.setDecimal(currency.decimal());
    }
}
