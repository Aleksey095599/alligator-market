package com.alligator.market.backend.sourcing.plan.api.command.create.controller;

import com.alligator.market.backend.sourcing.plan.api.command.create.dto.CreateInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.api.command.create.mapper.CreateInstrumentSourcePlanMapper;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateInstrumentSourcePlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер создания плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class CreateInstrumentSourcePlanController {

    private final CreateInstrumentSourcePlanService createInstrumentSourcePlanService;
    private final CreateInstrumentSourcePlanMapper createInstrumentSourcePlanMapper;

    public CreateInstrumentSourcePlanController(
            CreateInstrumentSourcePlanService createInstrumentSourcePlanService,
            CreateInstrumentSourcePlanMapper createInstrumentSourcePlanMapper
    ) {
        this.createInstrumentSourcePlanService = Objects.requireNonNull(
                createInstrumentSourcePlanService,
                "createInstrumentSourcePlanService must not be null"
        );
        this.createInstrumentSourcePlanMapper = Objects.requireNonNull(
                createInstrumentSourcePlanMapper,
                "createInstrumentSourcePlanMapper must not be null"
        );
    }

    /**
     * Создаёт новый план источников для инструмента.
     */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateInstrumentSourcePlanRequest request) {
        createInstrumentSourcePlanService.create(createInstrumentSourcePlanMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
