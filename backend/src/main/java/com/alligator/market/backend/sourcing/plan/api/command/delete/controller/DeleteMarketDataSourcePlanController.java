package com.alligator.market.backend.sourcing.plan.api.command.delete.controller;

import com.alligator.market.backend.sourcing.plan.application.command.delete.DeleteMarketDataSourcePlanService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер удаления плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/market-data-source-plans")
public class DeleteMarketDataSourcePlanController {

    private final DeleteMarketDataSourcePlanService deleteMarketDataSourcePlanService;

    public DeleteMarketDataSourcePlanController(
            DeleteMarketDataSourcePlanService deleteMarketDataSourcePlanService
    ) {
        this.deleteMarketDataSourcePlanService = Objects.requireNonNull(
                deleteMarketDataSourcePlanService,
                "deleteMarketDataSourcePlanService must not be null"
        );
    }

    /**
     * Удаляет план источников для заданного инструмента.
     */
    @DeleteMapping("/{captureProcessCode}/{instrumentCode}")
    public ResponseEntity<Void> delete(
            @PathVariable String captureProcessCode,
            @PathVariable String instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        deleteMarketDataSourcePlanService.delete(
                new MDCaptureProcessCode(captureProcessCode),
                new InstrumentCode(instrumentCode)
        );
        return ResponseEntity.noContent().build();
    }
}
