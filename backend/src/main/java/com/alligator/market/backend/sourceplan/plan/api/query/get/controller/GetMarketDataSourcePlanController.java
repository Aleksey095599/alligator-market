package com.alligator.market.backend.sourceplan.plan.api.query.get.controller;

import com.alligator.market.backend.sourceplan.plan.api.query.common.dto.MarketDataSourcePlanResponse;
import com.alligator.market.backend.sourceplan.plan.api.query.common.mapper.MarketDataSourcePlanResponseMapper;
import com.alligator.market.backend.sourceplan.plan.application.query.common.model.MarketDataSourcePlanQueryItem;
import com.alligator.market.backend.sourceplan.plan.application.query.get.GetMarketDataSourcePlanService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
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
@RequestMapping("/api/v1/market-data-source-plans")
public class GetMarketDataSourcePlanController {

    private final GetMarketDataSourcePlanService getMarketDataSourcePlanService;
    private final MarketDataSourcePlanResponseMapper responseMapper;

    public GetMarketDataSourcePlanController(
            GetMarketDataSourcePlanService getMarketDataSourcePlanService,
            MarketDataSourcePlanResponseMapper responseMapper
    ) {
        this.getMarketDataSourcePlanService = Objects.requireNonNull(
                getMarketDataSourcePlanService,
                "getMarketDataSourcePlanService must not be null"
        );
        this.responseMapper = Objects.requireNonNull(responseMapper, "responseMapper must not be null");
    }

    /**
     * Возвращает план источников для заданного инструмента.
     */
    @GetMapping("/{captureProcessCode}/{instrumentCode}")
    public ResponseEntity<MarketDataSourcePlanResponse> get(
            @PathVariable String captureProcessCode,
            @PathVariable String instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        MarketDataSourcePlanQueryItem plan = getMarketDataSourcePlanService.get(
                new MarketDataCaptureProcessCode(captureProcessCode),
                new InstrumentCode(instrumentCode)
        );
        return ResponseEntity.ok(responseMapper.toPlanResponse(plan));
    }
}
