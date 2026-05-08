package com.alligator.market.backend.process.twap.application.runner;

import com.alligator.market.backend.process.twap.application.FxSpotTwapCaptureOnceService;
import com.alligator.market.backend.process.twap.capturer.FxSpotTwapCapturer;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.captured.CapturedMarketDataTick;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class FxSpotTwapCaptureOnceRunner implements ApplicationRunner, DisposableBean {
    private static final String INSTRUMENT_CODE_OPTION = "instrument-code";
    private static final String DEFAULT_INSTRUMENT_CODE = "FOREX_SPOT_CNYRUB_TOM";

    private final FxSpotTwapCaptureOnceService service;
    private final FxSpotTwapCapturer capturer;

    private Disposable captureSubscription;

    public FxSpotTwapCaptureOnceRunner(FxSpotTwapCaptureOnceService service, FxSpotTwapCapturer capturer) {
        this.service = Objects.requireNonNull(service, "service must not be null");
        this.capturer = Objects.requireNonNull(capturer, "capturer must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        InstrumentCode instrumentCode = new InstrumentCode(instrumentCode(args));
        Duration captureInterval = capturer.policy().captureInterval();

        log.info(
                "FX Spot TWAP capture stream started: instrumentCode={}, captureInterval={}",
                instrumentCode.value(),
                captureInterval
        );

        captureSubscription = captureStream(instrumentCode, captureInterval)
                .subscribe(capturedTick -> log.info(
                        "FX Spot TWAP tick captured: instrumentCode={}, sourceCode={}, sourceTickType={}",
                        capturedTick.instrumentCode().value(),
                        capturedTick.sourceCode().value(),
                        capturedTick.sourceTick().sourceTickType()
                ), ex -> log.error(
                        "FX Spot TWAP capture stream stopped unexpectedly: instrumentCode={}",
                        instrumentCode.value(),
                        ex
                ));
    }

    @Override
    public void destroy() {
        if (captureSubscription != null && !captureSubscription.isDisposed()) {
            captureSubscription.dispose();
        }
    }

    private Flux<CapturedMarketDataTick> captureStream(
            InstrumentCode instrumentCode,
            Duration captureInterval
    ) {
        return Flux.defer(() -> Mono.fromCallable(() -> service.captureOnce(instrumentCode))
                        .subscribeOn(Schedulers.boundedElastic())
                        .doOnError(ex -> log.warn(
                                "FX Spot TWAP capture attempt failed: instrumentCode={}, reason={}",
                                instrumentCode.value(),
                                ex.getMessage(),
                                ex
                        ))
                        .onErrorResume(ex -> Mono.empty())
                        .flux()
                )
                .repeatWhen(repeat -> repeat.delayElements(captureInterval));
    }

    private static String instrumentCode(ApplicationArguments args) {
        List<String> values = args.getOptionValues(INSTRUMENT_CODE_OPTION);

        if (values == null || values.isEmpty()) {
            return DEFAULT_INSTRUMENT_CODE;
        }

        String value = values.getFirst();

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("instrument-code must not be blank");
        }

        return value;
    }
}
