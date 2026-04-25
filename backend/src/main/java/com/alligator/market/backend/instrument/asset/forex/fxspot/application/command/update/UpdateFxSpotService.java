package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.dto.UpdateFxSpotCommand;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Use-case сервис обновления инструмента FOREX_SPOT.
 */
@Slf4j
public final class UpdateFxSpotService {

    private final FxSpotRepository fxSpotRepository;

    public UpdateFxSpotService(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository,
                "fxSpotRepository must not be null");
    }

    public void update(UpdateFxSpotCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        FxSpot current = fxSpotRepository.findByCode(command.instrumentCode())
                .orElseThrow(() -> new FxSpotNotFoundException(command.instrumentCode()));

        FxSpot updated = new FxSpot(
                current.base(),
                current.quote(),
                current.tenor(),
                command.defaultQuoteFractionDigits()
        );

        if (current.equals(updated)) {
            log.debug("FX_SPOT instrument {} update skipped: nothing to change", command.instrumentCode().value());
            return;
        }

        fxSpotRepository.update(updated);
        log.info("FX_SPOT instrument {} updated", updated.instrumentCode().value());
    }
}
