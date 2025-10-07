package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.springframework.stereotype.Component;

/**
 * Маппер: JPA-сущность ⇄ доменная модель.
 */
@Component
public class FxSpotEntityMapper {

    /** JPA-сущность ⇒ доменная модель. */
    public FxSpot toDomain(FxSpotEntity entity) {
        // Собираем доменную модель
        return new FxSpot(
                CurrencyEntityMapper.toDomain(entity.getBaseCurrency()),
                CurrencyEntityMapper.toDomain(entity.getQuoteCurrency()),
                entity.getValueDateCode(),
                entity.getQuoteDecimal()
        );
    }

    /** Обновление JPA-сущности. */
    public void updateEntity(FxSpot model,
                             CurrencyEntity base,
                             CurrencyEntity quote,
                             FxSpotEntity entity) {
        // Переносим значения в JPA-сущность
        entity.setBaseCurrency(base);
        entity.setQuoteCurrency(quote);
        entity.setQuoteDecimal(model.quoteDecimal());
        entity.setValueDateCode(model.valueDateCode());
        entity.setCode(model.code());
        entity.setType(model.type());
    }
}

