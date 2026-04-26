package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.mapper;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto.FxSpotListItemResponse;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;

import java.util.Objects;

/**
 * Маппер доменной модели FOREX_SPOT в API-ответ списка.
 */
public final class FxSpotListItemResponseMapper {

    private FxSpotListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static FxSpotListItemResponse toResponse(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        return new FxSpotListItemResponse(
                fxSpot.instrumentCode().value(),
                fxSpot.instrumentSymbol().value(),
                fxSpot.asset().name(),
                fxSpot.product().name(),
                fxSpot.base().code().value(),
                fxSpot.quote().code().value(),
                fxSpot.defaultQuoteFractionDigits(),
                fxSpot.tenor()
        );
    }
}
