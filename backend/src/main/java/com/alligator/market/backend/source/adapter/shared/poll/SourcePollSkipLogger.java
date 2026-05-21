package com.alligator.market.backend.source.adapter.shared.poll;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class SourcePollSkipLogger {
    private static final Logger POLL_EVENT_LOG = LoggerFactory.getLogger("market.source.poll");

    private SourcePollSkipLogger() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void logSkippedPoll(
            String streamName,
            InstrumentCode instrumentCode,
            Throwable error
    ) {
        Objects.requireNonNull(streamName, "streamName must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(error, "error must not be null");

        SourcePollSkipReason reason = SourcePollSkipReason.from(error);

        POLL_EVENT_LOG.warn(
                "{} poll skipped: instrumentCode={}, reason={}, message={}",
                streamName,
                instrumentCode.value(),
                reason,
                error.getMessage()
        );
        POLL_EVENT_LOG.debug(
                "{} poll failure details: instrumentCode={}, reason={}",
                streamName,
                instrumentCode.value(),
                reason,
                error
        );
    }
}
