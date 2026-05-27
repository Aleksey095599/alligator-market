package com.alligator.market.backend.process.quotemonitor.api.livequote.controller;

import com.alligator.market.backend.process.quotemonitor.api.livequote.dto.QuoteMonitorLiveQuoteDto;
import com.alligator.market.backend.process.quotemonitor.api.livequote.dto.QuoteMonitorLiveQuoteListResponse;
import com.alligator.market.backend.process.quotemonitor.application.tick.QuoteMonitorLastPriceCapturedTickQueryService;
import com.alligator.market.backend.process.quotemonitor.application.tick.QuoteMonitorLastPriceCapturedTickStreamService;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.QuoteMonitorLastPriceCapturedTick;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/quote-monitor/live-quotes")
public class QuoteMonitorLiveQuoteController {
    private final QuoteMonitorLastPriceCapturedTickQueryService queryService;
    private final QuoteMonitorLastPriceCapturedTickStreamService streamService;

    public QuoteMonitorLiveQuoteController(
            QuoteMonitorLastPriceCapturedTickQueryService queryService,
            QuoteMonitorLastPriceCapturedTickStreamService streamService
    ) {
        this.queryService = Objects.requireNonNull(queryService, "queryService must not be null");
        this.streamService = Objects.requireNonNull(streamService, "streamService must not be null");
    }

    @GetMapping("/snapshot")
    public ResponseEntity<QuoteMonitorLiveQuoteListResponse> snapshot() {
        return ResponseEntity.ok(new QuoteMonitorLiveQuoteListResponse(
                queryService.currentTicks()
                        .stream()
                        .map(this::toDto)
                        .toList()
        ));
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<QuoteMonitorLiveQuoteDto>> stream() {
        return streamService.streamTicks()
                .map(tick -> ServerSentEvent.builder(toDto(tick))
                        .event("live-quote")
                        .id(tick.instrumentCode().value() + ":" + tick.receivedTimestamp())
                        .build());
    }

    private QuoteMonitorLiveQuoteDto toDto(QuoteMonitorLastPriceCapturedTick tick) {
        return new QuoteMonitorLiveQuoteDto(
                tick.instrumentCode().value(),
                tick.sourceTick().lastPrice().toPlainString(),
                tick.sourceCode().value(),
                tick.sourceTick().sourceTickTime(),
                tick.receivedTimestamp()
        );
    }
}
