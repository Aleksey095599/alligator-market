package com.alligator.market.backend.sourceplan.plan.api.query.get.controller;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.SourcePlanResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.SourcePlanResponseMapper;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.SourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.get.GetSourcePlanService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
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
@RequestMapping("/api/v1/source-plans")
public class GetSourcePlanController {

    private final GetSourcePlanService getSourcePlanService;
    private final SourcePlanResponseMapper responseMapper;

    public GetSourcePlanController(
            GetSourcePlanService getSourcePlanService,
            SourcePlanResponseMapper responseMapper
    ) {
        this.getSourcePlanService = Objects.requireNonNull(
                getSourcePlanService,
                "getSourcePlanService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает план источников для заданного инструмента.
     */
    @GetMapping("/{capturerCode}/{instrumentCode}")
    public ResponseEntity<SourcePlanResponse> get(
            @PathVariable String capturerCode,
            @PathVariable String instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        SourcePlanQueryItem plan = getSourcePlanService.get(
                new MarketDataCapturerCode(capturerCode),
                new InstrumentCode(instrumentCode)
        );
        return ResponseEntity.ok(responseMapper.toPlanResponse(plan));
    }
}
