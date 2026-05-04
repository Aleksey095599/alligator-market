package com.alligator.market.backend.marketdata.capture.application.port.adapter;

import com.alligator.market.backend.marketdata.capture.application.exception.CaptureInstrumentNotFoundException;
import com.alligator.market.backend.marketdata.capture.application.port.MarketDataCaptureInstrumentResolver;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * FX_SPOT-адаптер разрешения доменного инструмента для capture-сценариев.
 */
public final class FxSpotMarketDataCaptureInstrumentResolverAdapter
        implements MarketDataCaptureInstrumentResolver {

    private final FxSpotRepository fxSpotRepository;

    public FxSpotMarketDataCaptureInstrumentResolverAdapter(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository, "fxSpotRepository must not be null");
    }

    @Override
    public Instrument resolveRequired(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return fxSpotRepository.findByCode(instrumentCode)
                .orElseThrow(() -> new CaptureInstrumentNotFoundException(instrumentCode));
    }
}
