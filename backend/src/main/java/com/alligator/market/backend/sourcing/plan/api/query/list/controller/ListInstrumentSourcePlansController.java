package com.alligator.market.backend.sourcing.plan.api.query.list.controller;

import com.alligator.market.backend.sourcing.plan.api.query.get.dto.InstrumentSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.get.dto.MarketDataSourceResponse;
import com.alligator.market.backend.sourcing.plan.api.query.list.dto.InstrumentSourcePlanListResponse;
import com.alligator.market.backend.sourcing.plan.application.query.list.ListInstrumentSourcePlansService;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
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

    public ListInstrumentSourcePlansController(ListInstrumentSourcePlansService listInstrumentSourcePlansService) {
        this.listInstrumentSourcePlansService = Objects.requireNonNull(
                listInstrumentSourcePlansService,
                "listInstrumentSourcePlansService must not be null"
        );
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    @GetMapping
    public ResponseEntity<InstrumentSourcePlanListResponse> list() {
        List<InstrumentSourcePlanResponse> plans = listInstrumentSourcePlansService.list().stream()
                .map(this::toPlanResponse)
                .toList();

        return ResponseEntity.ok(new InstrumentSourcePlanListResponse(plans));
    }

    /* Маппинг доменного плана в HTTP-модель плана. */
    private InstrumentSourcePlanResponse toPlanResponse(InstrumentSourcePlan plan) {
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
