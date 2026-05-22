package com.alligator.market.backend.source.adapter.shared.poll;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.ssl.SslHandshakeTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;

class SourcePollSkipReasonTest {

    @Test
    void resolvesConnectionTimeoutFromCauseChain() {
        RuntimeException error = new RuntimeException(
                "request failed",
                new ConnectTimeoutException("connection timed out after 3000 ms")
        );

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.CONNECTION_TIMEOUT);
    }

    @Test
    void resolvesSslHandshakeTimeoutFromCauseChain() {
        RuntimeException error = new RuntimeException(
                "request failed",
                new SslHandshakeTimeoutException("handshake timed out after 10000ms")
        );

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.SSL_HANDSHAKE_TIMEOUT);
    }

    @Test
    void resolvesSslHandshakeTimeoutFromMessage() {
        RuntimeException error = new RuntimeException("handshake timed out after 10000ms");

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.SSL_HANDSHAKE_TIMEOUT);
    }

    @Test
    void resolvesResponseTimeoutFromCauseChain() {
        RuntimeException error = new RuntimeException("request failed", ReadTimeoutException.INSTANCE);

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.RESPONSE_TIMEOUT);
    }

    @Test
    void resolvesHttpErrorFromMessage() {
        IllegalStateException error = new IllegalStateException("MOEX ISS HTTP error 503");

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.HTTP_ERROR);
    }

    @Test
    void resolvesHttpErrorFromTypedPollHttpException() {
        SourcePollHttpException error = new SourcePollHttpException(
                HttpStatusCode.valueOf(503),
                "Service unavailable",
                "MOEX ISS FX_SPOT secid=USDRUB_TOM"
        );

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.HTTP_ERROR);
    }

    @Test
    void resolvesInvalidResponseFromIllegalState() {
        IllegalStateException error = new IllegalStateException("MOEX ISS response has no 'marketdata' object");

        assertThat(SourcePollSkipReason.from(error))
                .isEqualTo(SourcePollSkipReason.INVALID_RESPONSE);
    }
}
