package com.alligator.market.backend.process.twap.application.runner;

import com.alligator.market.backend.process.twap.application.FxSpotTwapCaptureOnceService;
import com.alligator.market.backend.process.twap.capturer.FxSpotTwapCapturer;
import com.alligator.market.domain.marketdata.tick.level.captured.CapturedMarketDataTick;
import com.alligator.market.domain.sourceplan.SourcePlan;
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
    private final FxSpotTwapCaptureOnceService service;
    private final FxSpotTwapCapturer capturer;

    private Disposable captureSubscription;

    public FxSpotTwapCaptureOnceRunner(FxSpotTwapCaptureOnceService service, FxSpotTwapCapturer capturer) {
        this.service = Objects.requireNonNull(service, "service must not be null");
        this.capturer = Objects.requireNonNull(capturer, "capturer must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        Duration captureInterval = capturer.policy().captureInterval();
        List<SourcePlan> sourcePlans = service.findExecutableSourcePlans(capturer.capturerCode());

        if (sourcePlans.isEmpty()) {
            log.warn(
                    "FX Spot TWAP capture stream not started: capturerCode={}, reason=no executable source plans",
                    capturer.capturerCode().value()
            );
            return;
        }

        log.info(
                "FX Spot TWAP capture streams started: capturerCode={}, sourcePlanCount={}, captureInterval={}",
                capturer.capturerCode().value(),
                sourcePlans.size(),
                captureInterval
        );

        captureSubscription = Flux.fromIterable(sourcePlans)
                .flatMap(sourcePlan -> captureStream(sourcePlan, captureInterval))
                .subscribe(capturedTick -> log.info(
                        "FX Spot TWAP tick captured: instrumentCode={}, sourceCode={}, sourceTickType={}",
                        capturedTick.instrumentCode().value(),
                        capturedTick.sourceCode().value(),
                        capturedTick.sourceTick().sourceTickType()
                ), ex -> log.error(
                        "FX Spot TWAP capture streams stopped unexpectedly: capturerCode={}",
                        capturer.capturerCode().value(),
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
            SourcePlan sourcePlan,
            Duration captureInterval
    ) {
        return Flux.defer(() -> Mono.fromCallable(() -> service.captureOnce(sourcePlan))
                        .subscribeOn(Schedulers.boundedElastic())
                        .doOnError(ex -> log.warn(
                                "FX Spot TWAP capture attempt failed: capturerCode={}, instrumentCode={}, reason={}",
                                sourcePlan.capturerCode().value(),
                                sourcePlan.instrumentCode().value(),
                                ex.getMessage(),
                                ex
                        ))
                        .onErrorResume(ex -> Mono.empty())
                        .flux()
                )
                .repeatWhen(repeat -> repeat.delayElements(captureInterval));
    }
}
