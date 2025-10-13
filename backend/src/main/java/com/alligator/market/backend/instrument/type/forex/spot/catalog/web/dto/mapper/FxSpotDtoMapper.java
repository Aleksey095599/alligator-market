package com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.mapper;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.out.FxSpotListItemDto;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Маппер: модель ⇄ DTO.
 */
@Component
public class FxSpotDtoMapper {

    /** Приватный конструктор (запрещает создание экземпляров). */
    private FxSpotDtoMapper() { throw new UnsupportedOperationException("Utility class"); }

    /** Преобразует доменную модель в DTO для списка. */
    public FxSpotListItemDto toListItemDto(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        return new FxSpotListItemDto(
                fxSpot.instrumentSymbol(),
                fxSpot.base().code().value(),
                fxSpot.quote().code().value(),
                fxSpot.defaultQuoteFractionDigits(),
                fxSpot.valueDate()
        );
    }
}

