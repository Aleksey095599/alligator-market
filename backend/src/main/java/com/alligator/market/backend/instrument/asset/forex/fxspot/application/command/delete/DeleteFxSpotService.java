package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.delete;

import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Use-case сервис удаления инструмента FOREX_SPOT.
 */
@Slf4j
public final class DeleteFxSpotService {

    private final FxSpotRepository fxSpotRepository;

    public DeleteFxSpotService(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository, "fxSpotRepository must not be null");
    }

    public void delete(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        fxSpotRepository.deleteByCode(instrumentCode);
        log.info("FX_SPOT instrument {} deleted", instrumentCode.value());
    }
}
