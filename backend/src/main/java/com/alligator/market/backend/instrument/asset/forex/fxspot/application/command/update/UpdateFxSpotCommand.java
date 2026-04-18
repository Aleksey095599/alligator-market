package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update;

import com.alligator.market.domain.instrument.base.vo.InstrumentCode;

/**
 * Команда use case обновления инструмента FOREX_SPOT.
 */
public record UpdateFxSpotCommand(
        InstrumentCode instrumentCode,
        Integer defaultQuoteFractionDigits
) {
}
