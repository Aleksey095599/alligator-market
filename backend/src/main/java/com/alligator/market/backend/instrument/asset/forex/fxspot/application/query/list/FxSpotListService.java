package com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.list;

import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Use-case сервис списка инструментов FOREX_SPOT.
 */
@Slf4j
public final class FxSpotListService {

    private final FxSpotRepository fxSpotRepository;

    public FxSpotListService(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository,
                "fxSpotRepository must not be null");
    }

    public List<FxSpot> findAll() {
        List<FxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
