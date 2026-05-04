package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.mapper;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.dto.CreateFxSpotRequest;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.CreateFxSpotCommand;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Маппер API-запроса создания в команду приложения.
 */
public final class CreateFxSpotRequestMapper {

    private CreateFxSpotRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CreateFxSpotCommand toCommand(CreateFxSpotRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        return new CreateFxSpotCommand(
                CurrencyCode.of(request.baseCurrency()),
                CurrencyCode.of(request.quoteCurrency()),
                request.tenor(),
                request.defaultQuoteFractionDigits()
        );
    }
}
