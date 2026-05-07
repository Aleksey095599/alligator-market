package com.alligator.market.backend.sourceplan.plan.api.command.create.controller;

import com.alligator.market.backend.sourceplan.plan.api.command.create.dto.CreateMarketDataSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.create.mapper.CreateMarketDataSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.application.command.create.CreateMarketDataSourcePlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер создания плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/market-data-source-plans")
public class CreateMarketDataSourcePlanController {

    private final CreateMarketDataSourcePlanService createMarketDataSourcePlanService;
    private final CreateMarketDataSourcePlanMapper createMarketDataSourcePlanMapper;

    public CreateMarketDataSourcePlanController(
            CreateMarketDataSourcePlanService createMarketDataSourcePlanService,
            CreateMarketDataSourcePlanMapper createMarketDataSourcePlanMapper
    ) {
        this.createMarketDataSourcePlanService = Objects.requireNonNull(
                createMarketDataSourcePlanService,
                "createMarketDataSourcePlanService must not be null"
        );
        this.createMarketDataSourcePlanMapper = Objects.requireNonNull(
                createMarketDataSourcePlanMapper,
                "createMarketDataSourcePlanMapper must not be null"
        );
    }

    /**
     * Создаёт новый план источников для инструмента.
     */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateMarketDataSourcePlanRequest request) {
        createMarketDataSourcePlanService.create(createMarketDataSourcePlanMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
