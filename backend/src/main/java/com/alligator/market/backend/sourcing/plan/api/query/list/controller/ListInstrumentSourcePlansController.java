package com.alligator.market.backend.sourcing.plan.api.query.list.controller;

import com.alligator.market.backend.sourcing.plan.api.query.get.dto.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.mapper.InstrumentSourcePlanResponseMapper;
import com.alligator.market.backend.sourcing.plan.api.query.list.dto.InstrumentSourcePlanListResponse;
import com.alligator.market.backend.sourcing.plan.application.query.list.ListInstrumentSourcePlansService;
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
public class ListInstrumentSourcePlansController {

    /* Сервис list-use case. */
    private final ListInstrumentSourcePlansService listInstrumentSourcePlansService;
    /* Единый mapper read-side DTO. */
    private final InstrumentSourcePlanResponseMapper responseMapper;

    public ListInstrumentSourcePlansController(
            ListInstrumentSourcePlansService listInstrumentSourcePlansService,
            InstrumentSourcePlanResponseMapper responseMapper
    ) {
        this.listInstrumentSourcePlansService = Objects.requireNonNull(
                listInstrumentSourcePlansService,
                "listInstrumentSourcePlansService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    @GetMapping
    public ResponseEntity<InstrumentSourcePlanListResponse> list() {
        List<InstrumentSourcePlanResponse> plans = listInstrumentSourcePlansService.list().stream()
                .map(responseMapper::toPlanResponse)
                .toList();

        return ResponseEntity.ok(new InstrumentSourcePlanListResponse(plans));
    }
}
