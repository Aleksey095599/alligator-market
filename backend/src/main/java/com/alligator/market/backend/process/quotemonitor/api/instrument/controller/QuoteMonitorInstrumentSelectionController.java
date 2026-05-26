package com.alligator.market.backend.process.quotemonitor.api.instrument.controller;

import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.QuoteMonitorInstrumentDto;
import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.QuoteMonitorInstrumentOptionDto;
import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.QuoteMonitorInstrumentOptionsResponse;
import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.QuoteMonitorInstrumentSelectionResponse;
import com.alligator.market.backend.process.quotemonitor.api.instrument.dto.ReplaceQuoteMonitorInstrumentSelectionRequest;
import com.alligator.market.backend.process.quotemonitor.application.instrument.QuoteMonitorInstrumentSelectionService;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorInstrumentOption;
import com.alligator.market.backend.process.quotemonitor.application.instrument.model.QuoteMonitorSelectedInstrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/quote-monitor/instrument-selection")
public class QuoteMonitorInstrumentSelectionController {
    private final QuoteMonitorInstrumentSelectionService service;

    public QuoteMonitorInstrumentSelectionController(QuoteMonitorInstrumentSelectionService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @GetMapping
    public ResponseEntity<QuoteMonitorInstrumentSelectionResponse> selected() {
        return ResponseEntity.ok(new QuoteMonitorInstrumentSelectionResponse(
                service.findSelectedInstruments()
                        .stream()
                        .map(this::toInstrumentDto)
                        .toList()
        ));
    }

    @GetMapping("/options")
    public ResponseEntity<QuoteMonitorInstrumentOptionsResponse> options() {
        return ResponseEntity.ok(new QuoteMonitorInstrumentOptionsResponse(
                service.findOptions()
                        .stream()
                        .map(this::toOptionDto)
                        .toList()
        ));
    }

    @PutMapping
    public ResponseEntity<Void> replace(
            @Valid @RequestBody ReplaceQuoteMonitorInstrumentSelectionRequest request
    ) {
        service.replaceSelection(request.instrumentCodes().stream()
                .map(InstrumentCode::new)
                .toList());

        return ResponseEntity.noContent().build();
    }

    private QuoteMonitorInstrumentDto toInstrumentDto(QuoteMonitorSelectedInstrument instrument) {
        return new QuoteMonitorInstrumentDto(
                instrument.instrumentCode().value(),
                instrument.sourcePlanStatus().name()
        );
    }

    private QuoteMonitorInstrumentOptionDto toOptionDto(QuoteMonitorInstrumentOption option) {
        return new QuoteMonitorInstrumentOptionDto(
                option.instrumentCode().value(),
                option.selected()
        );
    }
}
