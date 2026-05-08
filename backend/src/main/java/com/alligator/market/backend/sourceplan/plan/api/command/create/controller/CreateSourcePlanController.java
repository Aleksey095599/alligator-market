package com.alligator.market.backend.sourceplan.plan.api.command.create.controller;

import com.alligator.market.backend.sourceplan.plan.api.command.create.dto.CreateSourcePlanRequest;
import com.alligator.market.backend.sourceplan.plan.api.command.create.mapper.CreateSourcePlanMapper;
import com.alligator.market.backend.sourceplan.plan.application.command.create.CreateSourcePlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер создания плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/source-plans")
public class CreateSourcePlanController {

    private final CreateSourcePlanService createSourcePlanService;
    private final CreateSourcePlanMapper createSourcePlanMapper;

    public CreateSourcePlanController(
            CreateSourcePlanService createSourcePlanService,
            CreateSourcePlanMapper createSourcePlanMapper
    ) {
        this.createSourcePlanService = Objects.requireNonNull(
                createSourcePlanService,
                "createSourcePlanService must not be null"
        );
        this.createSourcePlanMapper = Objects.requireNonNull(
                createSourcePlanMapper,
                "createSourcePlanMapper must not be null"
        );
    }

    /**
     * Создаёт новый план источников для инструмента.
     */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateSourcePlanRequest request) {
        createSourcePlanService.create(createSourcePlanMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
