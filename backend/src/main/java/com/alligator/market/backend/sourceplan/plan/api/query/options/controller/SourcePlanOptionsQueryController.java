package com.alligator.market.backend.sourceplan.plan.api.query.options.controller;

import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.CapturerOptionDto;
import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.InstrumentOptionDto;
import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.SourcePlanOptionsResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.options.dto.SourceOptionDto;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.CapturerOptionsQueryPort;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.SourceOptionsQueryPort;
import com.alligator.market.domain.instrument.identity.InstrumentIdentityStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class SourcePlanOptionsQueryController {
    private final CapturerOptionsQueryPort capturerOptionsQueryPort;
    private final InstrumentIdentityStore instrumentIdentityStore;
    private final SourceOptionsQueryPort sourceOptionsQueryPort;

    public SourcePlanOptionsQueryController(
            CapturerOptionsQueryPort capturerOptionsQueryPort,
            InstrumentIdentityStore instrumentIdentityStore,
            SourceOptionsQueryPort sourceOptionsQueryPort
    ) {
        this.capturerOptionsQueryPort = Objects.requireNonNull(
                capturerOptionsQueryPort,
                "capturerOptionsQueryPort must not be null"
        );
        this.instrumentIdentityStore = Objects.requireNonNull(
                instrumentIdentityStore,
                "instrumentIdentityStore must not be null"
        );
        this.sourceOptionsQueryPort = Objects.requireNonNull(
                sourceOptionsQueryPort,
                "sourceOptionsQueryPort must not be null"
        );
    }

    @GetMapping("/api/v1/source-plans/options")
    public ResponseEntity<SourcePlanOptionsResponse> getOptions() {
        SourcePlanOptionsResponse response = new SourcePlanOptionsResponse(
                capturerOptionsQueryPort.findAllCapturers().stream()
                        .map(option -> new CapturerOptionDto(
                                option.code().value(),
                                option.displayName().value()
                        ))
                        .toList(),
                instrumentIdentityStore.instrumentCodes().stream()
                        .map(code -> new InstrumentOptionDto(code.value()))
                        .toList(),
                sourceOptionsQueryPort.findAllSourceCodes().stream()
                        .map(code -> new SourceOptionDto(code.value()))
                        .toList()
        );

        return ResponseEntity.ok(response);
    }
}
