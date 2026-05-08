package com.alligator.market.backend.process.twap.application.runner;

import com.alligator.market.backend.process.twap.application.FxSpotTwapCaptureOnceService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;
import java.util.Objects;

/**
 * Development runner for executing one FX Spot TWAP capture step on startup.
 */
@Slf4j
public final class FxSpotTwapCaptureOnceRunner implements ApplicationRunner {

    private static final String INSTRUMENT_CODE_OPTION = "instrument-code";
    private static final String DEFAULT_INSTRUMENT_CODE = "FOREX_SPOT_CNYRUB_TOM";

    private final FxSpotTwapCaptureOnceService service;

    public FxSpotTwapCaptureOnceRunner(FxSpotTwapCaptureOnceService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        InstrumentCode instrumentCode = new InstrumentCode(instrumentCode(args));

        log.info("FX Spot TWAP capture-once started: instrumentCode={}", instrumentCode.value());

        try {
            service.captureOnce(instrumentCode);
            log.info("FX Spot TWAP capture-once finished: instrumentCode={}", instrumentCode.value());
        } catch (RuntimeException ex) {
            log.error("FX Spot TWAP capture-once failed: instrumentCode={}", instrumentCode.value(), ex);
            throw ex;
        }
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
