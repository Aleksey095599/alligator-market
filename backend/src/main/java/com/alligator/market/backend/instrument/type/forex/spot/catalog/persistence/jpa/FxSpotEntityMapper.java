package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.Objects;

/**
 * Маппер JPA-сущности в доменную модель FX_SPOT.
 */
public final class FxSpotEntityMapper {

    /**
     * Приватный конструктор.
     */
    private FxSpotEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Преобразует JPA-сущность в доменную модель: {@link FxSpotEntity} --> {@link FxSpot}
     */
    public static FxSpot toDomain(FxSpotEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // Поле defaultQuoteFractionDigits не является идентичностью сущности, поэтому может быть null.
        // Однако по бизнес логике доменная модель не предполагает возможность null => нужна null проверка.
        int digits = Objects.requireNonNull(e.getDefaultQuoteFractionDigits(),
                "defaultQuoteFractionDigits must not be null");

        // Собираем и возвращаем доменную модель
        return new FxSpot(
                CurrencyEntityMapper.toDomain(e.getBaseCurrency()),
                CurrencyEntityMapper.toDomain(e.getQuoteCurrency()),
                e.getTenor(),
                digits
        );
    }
}
