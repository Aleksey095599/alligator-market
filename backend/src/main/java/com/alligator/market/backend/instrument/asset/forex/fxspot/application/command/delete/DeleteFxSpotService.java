package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.delete;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotInUseException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.FxSpotUsageCheckPort;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Use-case сервис удаления инструмента FOREX_SPOT.
 */
@Slf4j
public final class DeleteFxSpotService {

    private final FxSpotRepository fxSpotRepository;
    private final FxSpotUsageCheckPort fxSpotUsageCheckPort;

    public DeleteFxSpotService(
            FxSpotRepository fxSpotRepository,
            FxSpotUsageCheckPort fxSpotUsageCheckPort
    ) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository,
                "fxSpotRepository must not be null");
        this.fxSpotUsageCheckPort = Objects.requireNonNull(fxSpotUsageCheckPort,
                "fxSpotUsageCheckPort must not be null");
    }

    public void delete(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Бизнес‑правило: удаление запрещено, пока инструмент используется в других фичах.
        if (fxSpotUsageCheckPort.isUsed(instrumentCode)) {
            throw new FxSpotInUseException(instrumentCode);
        }

        fxSpotRepository.deleteByCode(instrumentCode);
        log.info("FX_SPOT instrument {} deleted", instrumentCode.value());
    }
}
