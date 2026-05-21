package com.alligator.market.backend.source.adapter.shared.poll;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.slf4j.Logger;

import java.util.Objects;

public final class SourcePollSkipLogger {
    private SourcePollSkipLogger() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void logSkippedPoll(
            Logger log,
            String streamName,
            InstrumentCode instrumentCode,
            Throwable error
    ) {
        Objects.requireNonNull(log, "log must not be null");
        Objects.requireNonNull(streamName, "streamName must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(error, "error must not be null");

        SourcePollSkipReason reason = SourcePollSkipReason.from(error);

        log.warn(
                "{} poll skipped: instrumentCode={}, reason={}, message={}",
                streamName,
                instrumentCode.value(),
                reason,
                error.getMessage()
        );
        log.debug(
                "{} poll failure details: instrumentCode={}, reason={}",
                streamName,
                instrumentCode.value(),
                reason,
                error
        );
    }
}
