package com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.mapper;

import com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa.FxOutrightEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;

/**
 * Маппер между сущностью FX_OUTRIGHT и доменной моделью.
 */
public final class FxOutrightEntityMapper {

    private FxOutrightEntityMapper() {
    }

    /** Преобразует сущность в доменную модель. */
    public static FxOutright toDomain(FxOutrightEntity entity) {
        return new FxOutright(
                entity.getBaseCurrency().getCode(),
                entity.getQuoteCurrency().getCode(),
                entity.getQuoteDecimal(),
                entity.getValueDateCode()
        );
    }

    /** Заполняет сущность данными из доменной модели. */
    public static void toEntity(
            FxOutright model,
            FxOutrightEntity entity,
            CurrencyEntity baseCurrency,
            CurrencyEntity quoteCurrency
    ) {
        entity.setBaseCurrency(baseCurrency);
        entity.setQuoteCurrency(quoteCurrency);
        entity.setQuoteDecimal(model.quoteDecimal());
        entity.setValueDateCode(model.valueDateCode());
    }
}
