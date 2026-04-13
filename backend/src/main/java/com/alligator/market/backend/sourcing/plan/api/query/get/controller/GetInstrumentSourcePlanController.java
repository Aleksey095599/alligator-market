package com.alligator.market.backend.sourcing.plan.api.query.get.controller;

import com.alligator.market.backend.sourcing.plan.api.query.common.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.InstrumentSourcePlanResponseMapper;
import com.alligator.market.backend.sourcing.plan.application.query.get.GetInstrumentSourcePlanService;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * REST-адаптер чтения одного плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class GetInstrumentSourcePlanController {

    private final GetInstrumentSourcePlanService getInstrumentSourcePlanService;
    private final InstrumentSourcePlanResponseMapper responseMapper;

    public GetInstrumentSourcePlanController(
            GetInstrumentSourcePlanService getInstrumentSourcePlanService,
            InstrumentSourcePlanResponseMapper responseMapper
    ) {
        this.getInstrumentSourcePlanService = Objects.requireNonNull(
                getInstrumentSourcePlanService,
                "getInstrumentSourcePlanService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает план источников для заданного инструмента.
     */
    @GetMapping("/{instrumentCode}")
    public ResponseEntity<InstrumentSourcePlanResponse> get(@PathVariable String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        InstrumentSourcePlan plan = getInstrumentSourcePlanService.get(new InstrumentCode(instrumentCode));
        return ResponseEntity.ok(responseMapper.toPlanResponse(plan));
    }
}
