package com.alligator.market.backend.process.quotemonitor.api.instrument.controller;

import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.QuoteMonitorInstrumentDto;
import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.QuoteMonitorInstrumentListResponse;
import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.ReplaceQuoteMonitorInstrumentsRequest;
import com.alligator.market.backend.process.quotemonitor.application.instrument.QuoteMonitorInstrumentService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/quote-monitor/instruments")
public class QuoteMonitorInstrumentController {
    private final QuoteMonitorInstrumentService service;

    public QuoteMonitorInstrumentController(QuoteMonitorInstrumentService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @GetMapping("/available")
    public ResponseEntity<QuoteMonitorInstrumentListResponse> available() {
        return ResponseEntity.ok(toResponse(service.findAvailableInstrumentCodes()));
    }

    @GetMapping
    public ResponseEntity<QuoteMonitorInstrumentListResponse> selected() {
        return ResponseEntity.ok(toResponse(service.findSelectedInstrumentCodes()));
    }

    @PutMapping
    public ResponseEntity<Void> replace(
            @Valid @RequestBody ReplaceQuoteMonitorInstrumentsRequest request
    ) {
        service.replaceSelectedInstrumentCodes(request.instrumentCodes().stream()
                .map(InstrumentCode::new)
                .toList());

        return ResponseEntity.noContent().build();
    }

    private QuoteMonitorInstrumentListResponse toResponse(List<InstrumentCode> instrumentCodes) {
        return new QuoteMonitorInstrumentListResponse(
                instrumentCodes.stream()
                        .map(instrumentCode -> new QuoteMonitorInstrumentDto(instrumentCode.value()))
                        .toList()
        );
    }
}
