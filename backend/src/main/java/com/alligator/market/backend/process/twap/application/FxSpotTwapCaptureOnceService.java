package com.alligator.market.backend.process.twap.application;

import com.alligator.market.backend.process.twap.application.exception.FxSpotTwapInstrumentNotFoundException;
import com.alligator.market.backend.process.twap.application.exception.FxSpotTwapSourceNotFoundException;
import com.alligator.market.backend.process.twap.application.exception.FxSpotTwapSourcePlanNotFoundException;
import com.alligator.market.backend.process.twap.application.exception.FxSpotTwapSourceTickNotReceivedException;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.captured.CapturedMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.process.twap.repository.FxSpotTwapCapturedTicksRepository;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.process.twap.capturer.FxSpotTwapCapturer.CAPTURER_CODE;

public final class FxSpotTwapCaptureOnceService {
    private static final Duration SOURCE_TICK_WAIT_TIMEOUT = Duration.ofSeconds(30);

    private final SourcePlanRepository sourcePlanRepository;
    private final MarketDataSourceRegistry sourceRegistry;
    private final FxSpotRepository fxSpotRepository;
    private final FxSpotTwapCapturedTicksRepository capturedTickRepository;
    private final Clock clock;

    public FxSpotTwapCaptureOnceService(
            SourcePlanRepository sourcePlanRepository,
            MarketDataSourceRegistry sourceRegistry,
            FxSpotRepository fxSpotRepository,
            FxSpotTwapCapturedTicksRepository capturedTickRepository,
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

    public CapturedMarketDataTick captureOnce(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        SourcePlan sourcePlan = sourcePlanRepository
                .findExecutableByMarketDataCapturerCodeAndInstrumentCode(CAPTURER_CODE, instrumentCode)
                .orElseThrow(() -> new FxSpotTwapSourcePlanNotFoundException(
                        CAPTURER_CODE,
                        instrumentCode
                ));

        return captureOnce(sourcePlan);
    }

    public CapturedMarketDataTick captureOnce(SourcePlan sourcePlan) {
        Objects.requireNonNull(sourcePlan, "sourcePlan must not be null");

        SourcePlanEntry entry = firstEntry(sourcePlan);
        MarketDataSource source = source(entry.sourceCode());
        FxSpot instrument = fxSpot(sourcePlan.instrumentCode());
        SourceMarketDataTick sourceTick = sourceTick(source, instrument);

        CapturedMarketDataTick capturedTick = new CapturedMarketDataTick(
                sourcePlan.capturerCode(),
                instrument.instrumentCode(),
                source.sourceCode(),
                sourceTick,
                clock.instant()
        );

        capturedTickRepository.save(capturedTick);
        return capturedTick;
    }

    public List<SourcePlan> findExecutableSourcePlans(MarketDataCapturerCode capturerCode) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");

        return sourcePlanRepository.findExecutableByMarketDataCapturerCode(capturerCode);
    }

    private SourcePlanEntry firstEntry(SourcePlan sourcePlan) {
        return sourcePlan.entries()
                .stream()
                .min(Comparator.comparingInt(SourcePlanEntry::priority))
                .orElseThrow(() -> new FxSpotTwapSourceNotFoundException(
                        sourcePlan.capturerCode(),
                        sourcePlan.instrumentCode()
                ));
    }

    private MarketDataSource source(MarketDataSourceCode sourceCode) {
        MarketDataSource source = sourceRegistry.sourcesByCode().get(sourceCode);

        if (source == null) {
            throw new FxSpotTwapSourceNotFoundException(sourceCode);
        }

        return source;
    }

    private FxSpot fxSpot(InstrumentCode instrumentCode) {
        return fxSpotRepository.findByCode(instrumentCode)
                .orElseThrow(() -> new FxSpotTwapInstrumentNotFoundException(instrumentCode));
    }

    private SourceMarketDataTick sourceTick(MarketDataSource source, FxSpot instrument) {
        SourceMarketDataTick sourceTick = Mono.from(source.streamSourceTicks(instrument))
                .block(SOURCE_TICK_WAIT_TIMEOUT);

        if (sourceTick == null) {
            throw new FxSpotTwapSourceTickNotReceivedException(
                    instrument.instrumentCode(),
                    source.sourceCode()
            );
        }

        return sourceTick;
    }
}
