package com.alligator.market.backend.sourcing.plan.api.command.delete.controller;

import com.alligator.market.backend.sourcing.plan.application.command.delete.DeleteInstrumentSourcePlanService;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * REST-адаптер удаления плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class DeleteInstrumentSourcePlanController {

    private final DeleteInstrumentSourcePlanService deleteInstrumentSourcePlanService;

    public DeleteInstrumentSourcePlanController(
            DeleteInstrumentSourcePlanService deleteInstrumentSourcePlanService
    ) {
        this.deleteInstrumentSourcePlanService = Objects.requireNonNull(
                deleteInstrumentSourcePlanService,
                "deleteInstrumentSourcePlanService must not be null"
        );
    }

    /**
     * Удаляет план источников для заданного инструмента.
     */
    @DeleteMapping("/{instrumentCode}")
    public ResponseEntity<Void> delete(@PathVariable String instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        deleteInstrumentSourcePlanService.delete(new InstrumentCode(instrumentCode));
        return ResponseEntity.noContent().build();
    }
}
