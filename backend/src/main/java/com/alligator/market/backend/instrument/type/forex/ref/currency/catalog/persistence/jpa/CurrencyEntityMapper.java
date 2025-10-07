package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Маппер: JPA-сущность ⇄ доменная модель.
 */
@Component
public class CurrencyEntityMapper {

    /** JPA-сущность ⇒ доменная модель. */
    public Currency toDomain(CurrencyEntity e) {
        int digits = Objects.requireNonNull(e.getDefaultFractionDigits(),
                "defaultFractionDigits must not be null");
        return new Currency(
                e.getCode(),
                e.getName(),
                e.getCountry(),
                digits
        );
    }

    /** Обновление JPA-сущности. */
    public void updateEntity(Currency c, CurrencyEntity e) {
        e.setCode(c.code());
        e.setName(c.name());
        e.setCountry(c.country());
        e.setDefaultFractionDigits(c.defaultFractionDigits());
    }
}
