package com.alligator.market.backend.process.quotemonitor.api.livequote.controller;

import com.alligator.market.backend.process.quotemonitor.api.livequote.dto.QuoteMonitorLiveQuoteDto;
import com.alligator.market.backend.process.quotemonitor.api.livequote.dto.QuoteMonitorLiveQuoteListResponse;
import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteQueryService;
import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/quote-monitor/live-quotes")
public class QuoteMonitorLiveQuoteController {
    private final QuoteMonitorLiveQuoteQueryService service;

    public QuoteMonitorLiveQuoteController(QuoteMonitorLiveQuoteQueryService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @GetMapping
    public ResponseEntity<QuoteMonitorLiveQuoteListResponse> currentQuotes() {
        return ResponseEntity.ok(new QuoteMonitorLiveQuoteListResponse(
                service.currentQuotes()
                        .stream()
                        .map(this::toDto)
                        .toList()
        ));
    }

    private QuoteMonitorLiveQuoteDto toDto(QuoteMonitorLiveQuote quote) {
        return new QuoteMonitorLiveQuoteDto(
                quote.instrumentCode().value(),
                quote.lastPrice().toPlainString(),
                quote.sourceCode().value(),
                quote.sourceTimestamp(),
                quote.receivedAt()
        );
    }
}
