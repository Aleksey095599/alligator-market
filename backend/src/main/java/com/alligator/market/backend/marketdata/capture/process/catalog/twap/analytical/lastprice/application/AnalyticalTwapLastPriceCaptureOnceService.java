package com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application;

import com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception.AnalyticalTwapLastPriceActiveSourceNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception.AnalyticalTwapLastPriceFxSpotInstrumentNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception.AnalyticalTwapLastPriceProviderNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception.AnalyticalTwapLastPriceSourcePlanNotFoundException;
import com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception.AnalyticalTwapLastPriceSourceTickNotReceivedException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.catalog.twap.analytical.lastprice.AnalyticalTwapLastPrice;
import com.alligator.market.domain.marketdata.tick.level.capture.CapturedMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.capture.repository.CapturedMarketDataTickRepository;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

/**
 * Use case одного шага фиксации тика для процесса {@code ANALYTICAL_TWAP_LAST_PRICE}.
 */
public final class AnalyticalTwapLastPriceCaptureOnceService {

    private static final Duration SOURCE_TICK_WAIT_TIMEOUT = Duration.ofSeconds(30);

    private final AnalyticalTwapLastPrice process;
    private final MarketDataSourcePlanRepository sourcePlanRepository;
    private final ProviderRegistry providerRegistry;
    private final FxSpotRepository fxSpotRepository;
    private final CapturedMarketDataTickRepository capturedTickRepository;
    private final Clock clock;

    public AnalyticalTwapLastPriceCaptureOnceService(
            AnalyticalTwapLastPrice process,
            MarketDataSourcePlanRepository sourcePlanRepository,
            ProviderRegistry providerRegistry,
            FxSpotRepository fxSpotRepository,
            CapturedMarketDataTickRepository capturedTickRepository,
            Clock clock
    ) {
        this.process = Objects.requireNonNull(process, "process must not be null");
        this.sourcePlanRepository = Objects.requireNonNull(sourcePlanRepository,
                "sourcePlanRepository must not be null");
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
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
                .findByCaptureProcessCodeAndInstrumentCode(process.processCode(), instrumentCode)
                .orElseThrow(() -> new AnalyticalTwapLastPriceSourcePlanNotFoundException(
                        process.processCode(),
                        instrumentCode
                ));

        MarketDataSource activeSource = firstActiveSource(sourcePlan);
        MarketDataProvider provider = provider(activeSource.providerCode());
        FxSpot instrument = fxSpot(instrumentCode);
        SourceMarketDataTick sourceTick = sourceTick(provider, instrument);

        CapturedMarketDataTick capturedTick = new CapturedMarketDataTick(
                process.processCode(),
                instrument.instrumentCode(),
                provider.providerCode(),
                sourceTick,
                clock.instant()
        );

        capturedTickRepository.save(capturedTick);
        return capturedTick;
    }

    private MarketDataSource firstActiveSource(MarketDataSourcePlan sourcePlan) {
        return sourcePlan.sources()
                .stream()
                .filter(MarketDataSource::active)
                .min(Comparator.comparingInt(MarketDataSource::priority))
                .orElseThrow(() -> new AnalyticalTwapLastPriceActiveSourceNotFoundException(
                        sourcePlan.captureProcessCode(),
                        sourcePlan.instrumentCode()
                ));
    }

    private MarketDataProvider provider(ProviderCode providerCode) {
        MarketDataProvider provider = providerRegistry.providersByCode().get(providerCode);

        if (provider == null) {
            throw new AnalyticalTwapLastPriceProviderNotFoundException(providerCode);
        }

        return provider;
    }

    private FxSpot fxSpot(InstrumentCode instrumentCode) {
        return fxSpotRepository.findByCode(instrumentCode)
                .orElseThrow(() -> new AnalyticalTwapLastPriceFxSpotInstrumentNotFoundException(instrumentCode));
    }

    private SourceMarketDataTick sourceTick(MarketDataProvider provider, FxSpot instrument) {
        SourceMarketDataTick sourceTick = Mono.from(provider.quote(instrument))
                .block(SOURCE_TICK_WAIT_TIMEOUT);

        if (sourceTick == null) {
            throw new AnalyticalTwapLastPriceSourceTickNotReceivedException(
                    instrument.instrumentCode(),
                    provider.providerCode()
            );
        }

        return sourceTick;
    }
}
