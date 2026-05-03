package com.alligator.market.backend.sourcing.plan.api.query.options.controller;

import com.alligator.market.backend.sourcing.plan.api.query.options.dto.InstrumentOptionDto;
import com.alligator.market.backend.sourcing.plan.api.query.options.dto.MarketDataSourcePlanOptionsResponse;
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
public class MarketDataSourcePlanOptionsQueryController {

    private final InstrumentOptionsQueryPort instrumentOptionsQueryPort;
    private final ProviderOptionsQueryPort providerOptionsQueryPort;

    public MarketDataSourcePlanOptionsQueryController(
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
    @GetMapping("/api/v1/market-data-source-plans/options")
    public ResponseEntity<MarketDataSourcePlanOptionsResponse> getOptions() {
        MarketDataSourcePlanOptionsResponse response = new MarketDataSourcePlanOptionsResponse(
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
