package com.alligator.market.backend.sourceplan.plan.api.query.list.controller;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourcePlanResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.SourcePlanResponseMapper;
import com.alligator.market.backend.sourceplan.plan.api.query.list.dto.SourcePlanListResponse;
import com.alligator.market.backend.sourceplan.plan.application.query.list.SourcePlanListService;
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
@RequestMapping("/api/v1/source-plans")
public class SourcePlanListController {

    private final SourcePlanListService sourcePlanListService;
    private final SourcePlanResponseMapper responseMapper;

    public SourcePlanListController(
            SourcePlanListService sourcePlanListService,
            SourcePlanResponseMapper responseMapper
    ) {
        this.sourcePlanListService = Objects.requireNonNull(
                sourcePlanListService,
                "listSourcePlansService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    @GetMapping
    public ResponseEntity<SourcePlanListResponse> list() {
        List<SourcePlanResponse> plans = sourcePlanListService.list().stream()
                .map(responseMapper::toPlanResponse)
                .toList();

        return ResponseEntity.ok(new SourcePlanListResponse(plans));
    }
}
