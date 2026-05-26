package com.alligator.market.backend.process.quotemonitor.api.runtime.controller;

import com.alligator.market.backend.process.quotemonitor.api.runtime.dto.QuoteMonitorInstrumentRuntimeStateResponse;
import com.alligator.market.backend.process.quotemonitor.api.runtime.dto.QuoteMonitorRuntimeStatusResponse;
import com.alligator.market.backend.process.quotemonitor.application.runtime.QuoteMonitorRuntimeControlService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeSnapshot;
import com.alligator.market.domain.process.quotemonitor.runtime.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.source.vo.SourceCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/quote-monitor/runtime")
public class QuoteMonitorRuntimeController {
    private final QuoteMonitorRuntimeControlService service;

    public QuoteMonitorRuntimeController(QuoteMonitorRuntimeControlService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @PostMapping("/start")
    public ResponseEntity<QuoteMonitorRuntimeStatusResponse> start() {
        return ResponseEntity.ok(toResponse(service.start()));
    }

    @PostMapping("/stop")
    public ResponseEntity<QuoteMonitorRuntimeStatusResponse> stop() {
        return ResponseEntity.ok(toResponse(service.stop()));
    }

    @GetMapping("/status")
    public ResponseEntity<QuoteMonitorRuntimeStatusResponse> status() {
        return ResponseEntity.ok(toResponse(service.snapshot()));
    }

    private QuoteMonitorRuntimeStatusResponse toResponse(
            QuoteMonitorRuntimeSnapshot snapshot
    ) {
        return new QuoteMonitorRuntimeStatusResponse(
                snapshot.status().name(),
                snapshot.monitoredInstrumentCodes()
                        .stream()
                        .map(InstrumentCode::value)
                        .toList(),
                snapshot.lastTickAt().orElse(null),
                snapshot.instrumentStates()
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private QuoteMonitorInstrumentRuntimeStateResponse toResponse(
            QuoteMonitorInstrumentRuntimeState state
    ) {
        return new QuoteMonitorInstrumentRuntimeStateResponse(
                state.instrumentCode().value(),
                state.sourceCode()
                        .map(SourceCode::value)
                        .orElse(null),
                state.status().name(),
                state.detail().orElse(null)
        );
    }
}
