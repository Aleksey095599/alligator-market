package com.alligator.market.backend.source.adapter.shared.poll;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class SourcePollSkipLogger {
    private static final int MAX_HTTP_RESPONSE_BODY_LENGTH = 1_000;
    private static final Logger POLL_EVENT_LOG = LoggerFactory.getLogger("market.source.poll");

    private SourcePollSkipLogger() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void logSkippedPoll(
            String streamName,
            SourceCode sourceCode,
            HandlerCode handlerCode,
            InstrumentCode instrumentCode,
            Throwable error
    ) {
        Objects.requireNonNull(streamName, "streamName must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(error, "error must not be null");

        SourcePollSkipReason reason = SourcePollSkipReason.from(error);
        String httpStatus = httpStatus(error);
        String httpResponseBody = httpResponseBody(error);

        POLL_EVENT_LOG.warn(
                "{} poll skipped: sourceCode={}, handlerCode={}, instrumentCode={}, reason={}, httpStatus={}, message={}",
                streamName,
                sourceCode.value(),
                handlerCode.value(),
                instrumentCode.value(),
                reason,
                httpStatus,
                error.getMessage()
        );
        POLL_EVENT_LOG.debug(
                "{} poll failure details: sourceCode={}, handlerCode={}, instrumentCode={}, reason={}, httpStatus={}, httpResponseBody={}",
                streamName,
                sourceCode.value(),
                handlerCode.value(),
                instrumentCode.value(),
                reason,
                httpStatus,
                httpResponseBody,
                error
        );
    }

    private static String httpStatus(Throwable error) {
        SourcePollHttpException httpError = httpError(error);
        if (httpError == null) {
            return "-";
        }

        return String.valueOf(httpError.statusCode().value());
    }

    private static String httpResponseBody(Throwable error) {
        SourcePollHttpException httpError = httpError(error);
        if (httpError == null) {
            return "-";
        }

        String responseBody = httpError.responseBody()
                .replace("\r", "\\r")
                .replace("\n", "\\n");
        if (responseBody.isBlank()) {
            return "-";
        }
        if (responseBody.length() <= MAX_HTTP_RESPONSE_BODY_LENGTH) {
            return responseBody;
        }

        return responseBody.substring(0, MAX_HTTP_RESPONSE_BODY_LENGTH) + "...";
    }

    private static SourcePollHttpException httpError(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof SourcePollHttpException httpError) {
                return httpError;
            }
            current = current.getCause();
        }

        return null;
    }
}
