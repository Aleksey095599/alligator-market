package com.alligator.market.backend.process.quotemonitor.api.runtime.controller;

import com.alligator.market.backend.process.quotemonitor.api.runtime.dto.LiveQuoteMonitorRuntimeStatusResponse;
import com.alligator.market.backend.process.quotemonitor.application.runtime.LiveQuoteMonitorRuntimeControlService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeSnapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/quote-monitor/runtime")
public class LiveQuoteMonitorRuntimeController {
    private final LiveQuoteMonitorRuntimeControlService service;

    public LiveQuoteMonitorRuntimeController(LiveQuoteMonitorRuntimeControlService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @PostMapping("/start")
    public ResponseEntity<LiveQuoteMonitorRuntimeStatusResponse> start() {
        return ResponseEntity.ok(toResponse(service.start()));
    }

    @PostMapping("/stop")
    public ResponseEntity<LiveQuoteMonitorRuntimeStatusResponse> stop() {
        return ResponseEntity.ok(toResponse(service.stop()));
    }

    @GetMapping("/status")
    public ResponseEntity<LiveQuoteMonitorRuntimeStatusResponse> status() {
        return ResponseEntity.ok(toResponse(service.snapshot()));
    }

    private LiveQuoteMonitorRuntimeStatusResponse toResponse(
            LiveQuoteMonitorRuntimeSnapshot snapshot
    ) {
        return new LiveQuoteMonitorRuntimeStatusResponse(
                snapshot.status().name(),
                snapshot.monitoredInstrumentCodes()
                        .stream()
                        .map(InstrumentCode::value)
                        .toList(),
                snapshot.lastTickAt().orElse(null)
        );
    }
}
