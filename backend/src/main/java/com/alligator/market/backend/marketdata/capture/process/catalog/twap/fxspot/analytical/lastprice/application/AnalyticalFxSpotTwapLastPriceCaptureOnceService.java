package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application;

import com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception.AnalyticalFxSpotTwapLastPriceInstrumentNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception.AnalyticalFxSpotTwapLastPriceSourceNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception.AnalyticalFxSpotTwapLastPriceSourcePlanNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception.AnalyticalFxSpotTwapLastPriceSourceTickNotReceivedException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.capture.CapturedMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.capture.repository.CapturedMarketDataTickRepository;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

import static com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.AnalyticalFxSpotTwapLastPriceCaptureProcess.PROCESS_CODE;

/**
 * Use case одного шага захвата тика для процесса {@code ANALYTICAL_FX_SPOT_TWAP_LAST_PRICE}.
 */
public final class AnalyticalFxSpotTwapLastPriceCaptureOnceService {

    private static final Duration SOURCE_TICK_WAIT_TIMEOUT = Duration.ofSeconds(30);

    private final MarketDataSourcePlanRepository sourcePlanRepository;
    private final MarketDataSourceRegistry sourceRegistry;
    private final FxSpotRepository fxSpotRepository;
    private final CapturedMarketDataTickRepository capturedTickRepository;
    private final Clock clock;

    public AnalyticalFxSpotTwapLastPriceCaptureOnceService(
            MarketDataSourcePlanRepository sourcePlanRepository,
            MarketDataSourceRegistry sourceRegistry,
            FxSpotRepository fxSpotRepository,
            CapturedMarketDataTickRepository capturedTickRepository,
            Clock clock
    ) {
        this.sourcePlanRepository = Objects.requireNonNull(sourcePlanRepository,
                "sourcePlanRepository must not be null");
        this.sourceRegistry = Objects.requireNonNull(sourceRegistry, "sourceRegistry must not be null");
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository, "fxSpotRepository must not be null");
        this.capturedTickRepository = Objects.requireNonNull(capturedTickRepository,
                "capturedTickRepository must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    /**
     * Выполняет один capture: выбирает источник, получает один source tick и сохраняет captured tick.
     */
    public CapturedMarketDataTick captureOnce(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        MarketDataSourcePlan sourcePlan = sourcePlanRepository
                .findActiveByMarketDataCaptureProcessCodeAndInstrumentCode(PROCESS_CODE, instrumentCode)
                .orElseThrow(() -> new AnalyticalFxSpotTwapLastPriceSourcePlanNotFoundException(
                        PROCESS_CODE,
                        instrumentCode
                ));

        MarketDataSourcePlanEntry entry = firstEntry(sourcePlan);
        MarketDataSource source = source(entry.sourceCode());
        FxSpot instrument = fxSpot(instrumentCode);
        SourceMarketDataTick sourceTick = sourceTick(source, instrument);

        CapturedMarketDataTick capturedTick = new CapturedMarketDataTick(
                PROCESS_CODE,
                instrument.instrumentCode(),
                source.sourceCode(),
                sourceTick,
                clock.instant()
        );

        capturedTickRepository.save(capturedTick);
        return capturedTick;
    }

    private MarketDataSourcePlanEntry firstEntry(MarketDataSourcePlan sourcePlan) {
        return sourcePlan.entries()
                .stream()
                .min(Comparator.comparingInt(MarketDataSourcePlanEntry::priority))
                .orElseThrow(() -> new AnalyticalFxSpotTwapLastPriceSourceNotFoundException(
                        sourcePlan.captureProcessCode(),
                        sourcePlan.instrumentCode()
                ));
    }

    private MarketDataSource source(MarketDataSourceCode sourceCode) {
        MarketDataSource source = sourceRegistry.sourcesByCode().get(sourceCode);

        if (source == null) {
            throw new AnalyticalFxSpotTwapLastPriceSourceNotFoundException(sourceCode);
        }

        return source;
    }

    private FxSpot fxSpot(InstrumentCode instrumentCode) {
        return fxSpotRepository.findByCode(instrumentCode)
                .orElseThrow(() -> new AnalyticalFxSpotTwapLastPriceInstrumentNotFoundException(instrumentCode));
    }

    private SourceMarketDataTick sourceTick(MarketDataSource source, FxSpot instrument) {
        SourceMarketDataTick sourceTick = Mono.from(source.streamSourceTicks(instrument))
                .block(SOURCE_TICK_WAIT_TIMEOUT);

        if (sourceTick == null) {
            throw new AnalyticalFxSpotTwapLastPriceSourceTickNotReceivedException(
                    instrument.instrumentCode(),
                    source.sourceCode()
            );
        }

        return sourceTick;
    }
}
