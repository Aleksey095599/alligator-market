package com.alligator.market.backend.sourcing.plan.api.create.controller;

import com.alligator.market.backend.sourcing.plan.api.create.dto.CreateInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.application.create.CreateInstrumentSourcePlanService;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * REST-адаптер создания плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class CreateInstrumentSourcePlanController {

    /* Сервис create-use case. */
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
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateInstrumentSourcePlanRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        createInstrumentSourcePlanService.create(toPlan(request));
    }

    /* Маппинг HTTP-запроса в доменный план. */
    private InstrumentSourcePlan toPlan(CreateInstrumentSourcePlanRequest request) {
        Objects.requireNonNull(request, "request must not be null");

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
            CreateInstrumentSourcePlanRequest.InstrumentMarketDataSourceRequest request
    ) {
        Objects.requireNonNull(request, "request must not be null");

        return new MarketDataSource(
                new ProviderCode(request.providerCode()),
                request.active(),
                request.priority()
        );
    }
}
