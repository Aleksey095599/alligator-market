package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class UpdateFxSpotService {
    private final FxSpotRepository fxSpotRepository;
    private final RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater;

    public UpdateFxSpotService(
            FxSpotRepository fxSpotRepository,
            RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater
    ) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository,
                "fxSpotRepository must not be null");
        this.runtimeInstrumentRegistryUpdater = Objects.requireNonNull(
                runtimeInstrumentRegistryUpdater,
                "runtimeInstrumentRegistryUpdater must not be null"
        );
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
            runtimeInstrumentRegistryUpdater.updateRuntimeRegistry();
            log.debug("FX_SPOT instrument {} update skipped: nothing to change", command.instrumentCode().value());
            return;
        }

        fxSpotRepository.update(updated);
        runtimeInstrumentRegistryUpdater.updateRuntimeRegistry();
        log.info("FX_SPOT instrument {} updated", updated.instrumentCode().value());
    }
}
