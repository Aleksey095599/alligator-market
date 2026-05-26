package com.alligator.market.backend.process.quotemonitor.api.livequote.controller;

import com.alligator.market.backend.process.quotemonitor.api.livequote.dto.QuoteMonitorInstrumentQuoteDto;
import com.alligator.market.backend.process.quotemonitor.api.livequote.dto.QuoteMonitorInstrumentQuoteListResponse;
import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteQueryService;
import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteStreamService;
import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
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
    private final QuoteMonitorLiveQuoteQueryService queryService;
    private final QuoteMonitorLiveQuoteStreamService streamService;

    public QuoteMonitorLiveQuoteController(
            QuoteMonitorLiveQuoteQueryService queryService,
            QuoteMonitorLiveQuoteStreamService streamService
    ) {
        this.queryService = Objects.requireNonNull(queryService, "queryService must not be null");
        this.streamService = Objects.requireNonNull(streamService, "streamService must not be null");
    }

    @GetMapping("/snapshot")
    public ResponseEntity<QuoteMonitorInstrumentQuoteListResponse> snapshot() {
        return ResponseEntity.ok(new QuoteMonitorInstrumentQuoteListResponse(
                queryService.currentQuotes()
                        .stream()
                        .map(this::toDto)
                        .toList()
        ));
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<QuoteMonitorInstrumentQuoteDto>> stream() {
        return streamService.streamQuotes()
                .map(quote -> ServerSentEvent.builder(toDto(quote))
                        .event("live-quote")
                        .id(quote.instrumentCode().value() + ":" + quote.receivedAt())
                        .build());
    }

    private QuoteMonitorInstrumentQuoteDto toDto(QuoteMonitorInstrumentQuote quote) {
        return new QuoteMonitorInstrumentQuoteDto(
                quote.instrumentCode().value(),
                quote.lastPrice().toPlainString(),
                quote.sourceCode().value(),
                quote.sourceTickTime(),
                quote.receivedAt()
        );
    }
}
