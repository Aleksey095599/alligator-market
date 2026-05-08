package com.alligator.market.backend.sourceplan.plan.api.query.options.controller;

import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.MarketDataCapturerOptionDto;
import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.InstrumentOptionDto;
import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.SourcePlanOptionsResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.MarketDataSourceOptionDto;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataCapturerOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.MarketDataSourceOptionsQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * REST-адаптер чтения options для экрана управления планами источников.
 */
@RestController
public class SourcePlanOptionsQueryController {

    private final MarketDataCapturerOptionsQueryPort capturerOptionsQueryPort;
    private final InstrumentOptionsQueryPort instrumentOptionsQueryPort;
    private final MarketDataSourceOptionsQueryPort sourceOptionsQueryPort;

    public SourcePlanOptionsQueryController(
            MarketDataCapturerOptionsQueryPort capturerOptionsQueryPort,
            InstrumentOptionsQueryPort instrumentOptionsQueryPort,
            MarketDataSourceOptionsQueryPort sourceOptionsQueryPort
    ) {
        this.capturerOptionsQueryPort = Objects.requireNonNull(
                capturerOptionsQueryPort,
                "capturerOptionsQueryPort must not be null"
        );
        this.instrumentOptionsQueryPort = Objects.requireNonNull(
                instrumentOptionsQueryPort,
                "instrumentOptionsQueryPort must not be null"
        );
        this.sourceOptionsQueryPort = Objects.requireNonNull(
                sourceOptionsQueryPort,
                "sourceOptionsQueryPort must not be null"
        );
    }

    /**
     * Возвращает данные для dropdown экрана управления планами источников.
     */
    @GetMapping("/api/v1/source-plans/options")
    public ResponseEntity<SourcePlanOptionsResponse> getOptions() {
        SourcePlanOptionsResponse response = new SourcePlanOptionsResponse(
                capturerOptionsQueryPort.findAllMarketDataCapturers().stream()
                        .map(option -> new MarketDataCapturerOptionDto(
                                option.code().value(),
                                option.displayName().value()
                        ))
                        .toList(),
                instrumentOptionsQueryPort.findAllInstrumentCodes().stream()
                        .map(code -> new InstrumentOptionDto(code.value()))
                        .toList(),
                sourceOptionsQueryPort.findAllSourceCodes().stream()
                        .map(code -> new MarketDataSourceOptionDto(code.value()))
                        .toList()
        );

        return ResponseEntity.ok(response);
    }
}
