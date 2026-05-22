package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.delete;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotInUseException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.FxSpotUsageCheckPort;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class DeleteFxSpotService {
    private final FxSpotRepository fxSpotRepository;
    private final FxSpotUsageCheckPort fxSpotUsageCheckPort;
    private final RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater;

    public DeleteFxSpotService(
            FxSpotRepository fxSpotRepository,
            FxSpotUsageCheckPort fxSpotUsageCheckPort,
            RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater
    ) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository,
                "fxSpotRepository must not be null");
        this.fxSpotUsageCheckPort = Objects.requireNonNull(fxSpotUsageCheckPort,
                "fxSpotUsageCheckPort must not be null");
        this.runtimeInstrumentRegistryUpdater = Objects.requireNonNull(
                runtimeInstrumentRegistryUpdater,
                "runtimeInstrumentRegistryUpdater must not be null"
        );
    }

    public void delete(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        if (fxSpotUsageCheckPort.isUsed(instrumentCode)) {
            throw new FxSpotInUseException(instrumentCode);
        }

        fxSpotRepository.deleteByCode(instrumentCode);
        runtimeInstrumentRegistryUpdater.updateRuntimeRegistry();
        log.info("FX_SPOT instrument {} deleted", instrumentCode.value());
    }
}
