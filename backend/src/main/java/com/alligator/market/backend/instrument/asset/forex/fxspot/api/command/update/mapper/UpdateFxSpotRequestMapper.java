package com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.mapper;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.dto.UpdateFxSpotRequest;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.dto.UpdateFxSpotCommand;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Маппер API-запроса обновления в команду приложения.
 */
public final class UpdateFxSpotRequestMapper {

    private UpdateFxSpotRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static UpdateFxSpotCommand toCommand(String instrumentCode, UpdateFxSpotRequest request) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(request, "request must not be null");

        return new UpdateFxSpotCommand(
                InstrumentCode.of(instrumentCode),
                request.defaultQuoteFractionDigits()
        );
    }
}
