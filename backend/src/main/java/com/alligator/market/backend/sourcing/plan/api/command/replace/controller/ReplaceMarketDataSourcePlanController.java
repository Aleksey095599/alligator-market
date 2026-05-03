package com.alligator.market.backend.sourcing.plan.api.command.replace.controller;

import com.alligator.market.backend.sourcing.plan.api.command.replace.dto.ReplaceMarketDataSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.command.replace.mapper.ReplaceMarketDataSourcePlanMapper;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceMarketDataSourcePlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер полной замены плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/market-data-source-plans")
public class ReplaceMarketDataSourcePlanController {

    private final ReplaceMarketDataSourcePlanService replaceMarketDataSourcePlanService;
    private final ReplaceMarketDataSourcePlanMapper replaceMarketDataSourcePlanMapper;

    public ReplaceMarketDataSourcePlanController(
            ReplaceMarketDataSourcePlanService replaceMarketDataSourcePlanService,
            ReplaceMarketDataSourcePlanMapper replaceMarketDataSourcePlanMapper
    ) {
        this.replaceMarketDataSourcePlanService = Objects.requireNonNull(
                replaceMarketDataSourcePlanService,
                "replaceMarketDataSourcePlanService must not be null"
        );
        this.replaceMarketDataSourcePlanMapper = Objects.requireNonNull(
                replaceMarketDataSourcePlanMapper,
                "replaceMarketDataSourcePlanMapper must not be null"
        );
    }

    /**
     * Полностью заменяет план источников для заданного инструмента.
     */
    @PutMapping("/{instrumentCode}")
    public ResponseEntity<Void> replace(
            @PathVariable String instrumentCode,
            @Valid @RequestBody ReplaceMarketDataSourcePlanRequest request
    ) {
        replaceMarketDataSourcePlanService.replace(replaceMarketDataSourcePlanMapper.toPlan(instrumentCode, request));
        return ResponseEntity.noContent().build();
    }
}
