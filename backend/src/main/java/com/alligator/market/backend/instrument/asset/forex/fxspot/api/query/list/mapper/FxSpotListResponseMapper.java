package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.mapper;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto.FxSpotListResponse;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;

import java.util.Objects;

/**
 * Маппер доменной модели FOREX_SPOT в API-ответ.
 */
public final class FxSpotListResponseMapper {

    private FxSpotListResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static FxSpotListResponse toResponse(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        return new FxSpotListResponse(
                fxSpot.instrumentCode().value(),
                fxSpot.instrumentSymbol().value(),
                fxSpot.assetClass().name(),
                fxSpot.contractType().name(),
                fxSpot.base().code().value(),
                fxSpot.quote().code().value(),
                fxSpot.defaultQuoteFractionDigits(),
                fxSpot.tenor()
        );
    }
}
