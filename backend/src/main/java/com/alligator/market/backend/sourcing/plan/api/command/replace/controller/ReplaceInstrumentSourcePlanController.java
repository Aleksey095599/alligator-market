package com.alligator.market.backend.sourcing.plan.api.command.replace.controller;

import com.alligator.market.backend.sourcing.plan.api.command.replace.dto.ReplaceInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.command.replace.mapper.ReplaceInstrumentSourcePlanMapper;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceInstrumentSourcePlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер полной замены плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class ReplaceInstrumentSourcePlanController {

    private final ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService;
    private final ReplaceInstrumentSourcePlanMapper replaceInstrumentSourcePlanMapper;

    public ReplaceInstrumentSourcePlanController(
            ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService,
            ReplaceInstrumentSourcePlanMapper replaceInstrumentSourcePlanMapper
    ) {
        this.replaceInstrumentSourcePlanService = Objects.requireNonNull(
                replaceInstrumentSourcePlanService,
                "replaceInstrumentSourcePlanService must not be null"
        );
        this.replaceInstrumentSourcePlanMapper = Objects.requireNonNull(
                replaceInstrumentSourcePlanMapper,
                "replaceInstrumentSourcePlanMapper must not be null"
        );
    }

    /**
     * Полностью заменяет план источников для заданного инструмента.
     */
    @PutMapping("/{instrumentCode}")
    public ResponseEntity<Void> replace(
            @PathVariable String instrumentCode,
            @Valid @RequestBody ReplaceInstrumentSourcePlanRequest request
    ) {
        replaceInstrumentSourcePlanService.replace(replaceInstrumentSourcePlanMapper.toPlan(instrumentCode, request));
        return ResponseEntity.noContent().build();
    }
}
