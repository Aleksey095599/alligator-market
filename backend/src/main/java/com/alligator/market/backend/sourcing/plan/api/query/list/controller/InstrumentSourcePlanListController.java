package com.alligator.market.backend.sourcing.plan.api.query.list.controller;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.mapper.InstrumentSourcePlanResponseMapper;
import com.alligator.market.backend.sourcing.plan.api.query.list.dto.InstrumentSourcePlanListResponse;
import com.alligator.market.backend.sourcing.plan.application.query.list.InstrumentSourcePlanListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST-адаптер чтения списка планов источников инструментов.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class InstrumentSourcePlanListController {

    private final InstrumentSourcePlanListService instrumentSourcePlanListService;
    private final InstrumentSourcePlanResponseMapper responseMapper;

    public InstrumentSourcePlanListController(
            InstrumentSourcePlanListService instrumentSourcePlanListService,
            InstrumentSourcePlanResponseMapper responseMapper
    ) {
        this.instrumentSourcePlanListService = Objects.requireNonNull(
                instrumentSourcePlanListService,
                "listInstrumentSourcePlansService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    @GetMapping
    public ResponseEntity<InstrumentSourcePlanListResponse> list() {
        List<InstrumentSourcePlanResponse> plans = instrumentSourcePlanListService.list().stream()
                .map(responseMapper::toPlanResponse)
                .toList();

        return ResponseEntity.ok(new InstrumentSourcePlanListResponse(plans));
    }
}
