package com.alligator.market.backend.sourcing.plan.api.query.list.controller;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourcePlanResponse;
import com.alligator.market.backend.sourcing.plan.api.query.common.mapper.MarketDataSourcePlanResponseMapper;
import com.alligator.market.backend.sourcing.plan.api.query.list.dto.MarketDataSourcePlanListResponse;
import com.alligator.market.backend.sourcing.plan.application.query.list.MarketDataSourcePlanListService;
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
@RequestMapping("/api/v1/market-data-source-plans")
public class MarketDataSourcePlanListController {

    private final MarketDataSourcePlanListService marketDataSourcePlanListService;
    private final MarketDataSourcePlanResponseMapper responseMapper;

    public MarketDataSourcePlanListController(
            MarketDataSourcePlanListService marketDataSourcePlanListService,
            MarketDataSourcePlanResponseMapper responseMapper
    ) {
        this.marketDataSourcePlanListService = Objects.requireNonNull(
                marketDataSourcePlanListService,
                "listMarketDataSourcePlansService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    @GetMapping
    public ResponseEntity<MarketDataSourcePlanListResponse> list() {
        List<MarketDataSourcePlanResponse> plans = marketDataSourcePlanListService.list().stream()
                .map(responseMapper::toPlanResponse)
                .toList();

        return ResponseEntity.ok(new MarketDataSourcePlanListResponse(plans));
    }
}
