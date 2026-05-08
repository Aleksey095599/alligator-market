package com.alligator.market.backend.sourceplan.plan.api.command.replace.controller;

import com.alligator.market.backend.sourceplan.plan.api.command.replace.dto.ReplaceSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.replace.mapper.ReplaceSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.application.command.replace.ReplaceSourcePlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер полной замены плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/source-plans")
public class ReplaceSourcePlanController {

    private final ReplaceSourcePlanService replaceSourcePlanService;
    private final ReplaceSourcePlanMapper replaceSourcePlanMapper;

    public ReplaceSourcePlanController(
            ReplaceSourcePlanService replaceSourcePlanService,
            ReplaceSourcePlanMapper replaceSourcePlanMapper
    ) {
        this.replaceSourcePlanService = Objects.requireNonNull(
                replaceSourcePlanService,
                "replaceSourcePlanService must not be null"
        );
        this.replaceSourcePlanMapper = Objects.requireNonNull(
                replaceSourcePlanMapper,
                "replaceSourcePlanMapper must not be null"
        );
    }

    /**
     * Полностью заменяет план источников для заданного инструмента.
     */
    @PutMapping("/{capturerCode}/{instrumentCode}")
    public ResponseEntity<Void> replace(
            @PathVariable String capturerCode,
            @PathVariable String instrumentCode,
            @Valid @RequestBody ReplaceSourcePlanRequest request
    ) {
        replaceSourcePlanService.replace(
                replaceSourcePlanMapper.toPlan(capturerCode, instrumentCode, request)
        );
        return ResponseEntity.noContent().build();
    }
}
