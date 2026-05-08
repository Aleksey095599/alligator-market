package com.alligator.market.backend.sourceplan.plan.api.command.delete.controller;

import com.alligator.market.backend.sourceplan.plan.application.command.delete.DeleteSourcePlanService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер удаления плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/source-plans")
public class DeleteSourcePlanController {

    private final DeleteSourcePlanService deleteSourcePlanService;

    public DeleteSourcePlanController(
            DeleteSourcePlanService deleteSourcePlanService
    ) {
        this.deleteSourcePlanService = Objects.requireNonNull(
                deleteSourcePlanService,
                "deleteSourcePlanService must not be null"
        );
    }

    /**
     * Удаляет план источников для заданного инструмента.
     */
    @DeleteMapping("/{capturerCode}/{instrumentCode}")
    public ResponseEntity<Void> delete(
            @PathVariable String capturerCode,
            @PathVariable String instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        deleteSourcePlanService.delete(
                new MarketDataCapturerCode(capturerCode),
                new InstrumentCode(instrumentCode)
        );
        return ResponseEntity.noContent().build();
    }
}
