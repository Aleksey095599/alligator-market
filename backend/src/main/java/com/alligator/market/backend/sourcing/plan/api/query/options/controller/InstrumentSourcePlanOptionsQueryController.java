package com.alligator.market.backend.sourcing.plan.api.query.options.controller;

import com.alligator.market.backend.sourcing.plan.api.query.options.dto.InstrumentOptionDto;
import com.alligator.market.backend.sourcing.plan.api.query.options.dto.InstrumentSourcePlanOptionsResponse;
import com.alligator.market.backend.sourcing.plan.api.query.options.dto.ProviderOptionDto;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.ProviderOptionsQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * REST-адаптер чтения options для экрана управления планами источников.
 */
@RestController
public class InstrumentSourcePlanOptionsQueryController {

    /* Query-порт получения доступных инструментов. */
    private final InstrumentOptionsQueryPort instrumentOptionsQueryPort;

    /* Query-порт получения доступных провайдеров. */
    private final ProviderOptionsQueryPort providerOptionsQueryPort;

    public InstrumentSourcePlanOptionsQueryController(
            InstrumentOptionsQueryPort instrumentOptionsQueryPort,
            ProviderOptionsQueryPort providerOptionsQueryPort
    ) {
        this.instrumentOptionsQueryPort = Objects.requireNonNull(
                instrumentOptionsQueryPort,
                "instrumentOptionsQueryPort must not be null"
        );
        this.providerOptionsQueryPort = Objects.requireNonNull(
                providerOptionsQueryPort,
                "providerOptionsQueryPort must not be null"
        );
    }

    /**
     * Возвращает данные для dropdown экрана управления планами источников.
     */
    @GetMapping("/api/v1/instrument-source-plans/options")
    public ResponseEntity<InstrumentSourcePlanOptionsResponse> getOptions() {
        InstrumentSourcePlanOptionsResponse response = new InstrumentSourcePlanOptionsResponse(
                instrumentOptionsQueryPort.findAllInstrumentCodes().stream()
                        .map(code -> new InstrumentOptionDto(code.value()))
                        .toList(),
                providerOptionsQueryPort.findAllProviderCodes().stream()
                        .map(code -> new ProviderOptionDto(code.value()))
                        .toList()
        );

        return ResponseEntity.ok(response);
    }
}
