package com.alligator.market.backend.sourcing.plan.api.command.create.controller;

import com.alligator.market.backend.sourcing.plan.api.common.dto.MarketDataSourceRequest;
import com.alligator.market.backend.sourcing.plan.api.command.create.dto.CreateInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateInstrumentSourcePlanService;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * REST-адаптер создания плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class CreateInstrumentSourcePlanController {

    private final CreateInstrumentSourcePlanService createInstrumentSourcePlanService;

    public CreateInstrumentSourcePlanController(
            CreateInstrumentSourcePlanService createInstrumentSourcePlanService
    ) {
        this.createInstrumentSourcePlanService = Objects.requireNonNull(
                createInstrumentSourcePlanService,
                "createInstrumentSourcePlanService must not be null"
        );
    }

    /**
     * Создаёт новый план источников для инструмента.
     */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateInstrumentSourcePlanRequest request) {
        createInstrumentSourcePlanService.create(toPlan(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* Маппинг HTTP-запроса в доменный план. */
    private InstrumentSourcePlan toPlan(CreateInstrumentSourcePlanRequest request) {
        List<MarketDataSource> sources = request.sources().stream()
                .map(this::toSource)
                .toList();

        return new InstrumentSourcePlan(
                new InstrumentCode(request.instrumentCode()),
                sources
        );
    }

    /* Маппинг HTTP-модели источника в доменный источник. */
    private MarketDataSource toSource(
            MarketDataSourceRequest request
    ) {
        return new MarketDataSource(
                new ProviderCode(request.providerCode()),
                request.active(),
                request.priority()
        );
    }
}
