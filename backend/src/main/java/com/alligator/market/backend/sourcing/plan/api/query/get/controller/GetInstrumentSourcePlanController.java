package com.alligator.market.backend.sourcing.plan.api.query.get.controller;

import com.alligator.market.backend.sourcing.plan.api.query.get.dto.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.get.dto.MarketDataSourceResponse;
import com.alligator.market.backend.sourcing.plan.application.query.get.GetInstrumentSourcePlanService;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST-адаптер чтения одного плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class GetInstrumentSourcePlanController {

    /* Сервис get-use case. */
    private final GetInstrumentSourcePlanService getInstrumentSourcePlanService;

    public GetInstrumentSourcePlanController(GetInstrumentSourcePlanService getInstrumentSourcePlanService) {
        this.getInstrumentSourcePlanService = Objects.requireNonNull(
                getInstrumentSourcePlanService,
                "getInstrumentSourcePlanService must not be null"
        );
    }

    /**
     * Возвращает план источников для заданного инструмента.
     */
    @GetMapping("/{instrumentCode}")
    public ResponseEntity<InstrumentSourcePlanResponse> get(@PathVariable String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        InstrumentSourcePlan plan = getInstrumentSourcePlanService.get(new InstrumentCode(instrumentCode));
        return ResponseEntity.ok(toResponse(plan));
    }

    /* Маппинг доменного плана в HTTP-ответ. */
    private InstrumentSourcePlanResponse toResponse(InstrumentSourcePlan plan) {
        List<MarketDataSourceResponse> sources = plan.sources().stream()
                .map(this::toSourceResponse)
                .toList();

        return new InstrumentSourcePlanResponse(plan.instrumentCode().value(), sources);
    }

    /* Маппинг доменного источника в HTTP-модель источника. */
    private MarketDataSourceResponse toSourceResponse(MarketDataSource source) {
        return new MarketDataSourceResponse(
                source.providerCode().value(),
                source.active(),
                source.priority()
        );
    }
}
