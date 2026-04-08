package com.alligator.market.backend.sourcing.plan.api.command.replace.controller;

import com.alligator.market.backend.sourcing.plan.api.command.replace.dto.ReplaceInstrumentSourcePlanRequest;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceInstrumentSourcePlanService;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * REST-адаптер полной замены плана источников инструмента.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class ReplaceInstrumentSourcePlanController {

    /* Сервис replace-use case. */
    private final ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService;

    public ReplaceInstrumentSourcePlanController(
            ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService
    ) {
        this.replaceInstrumentSourcePlanService = Objects.requireNonNull(
                replaceInstrumentSourcePlanService,
                "replaceInstrumentSourcePlanService must not be null"
        );
    }

    /**
     * Полностью заменяет план источников для заданного инструмента.
     */
    @PutMapping("/{instrumentCode}")
    public ResponseEntity<Void> replace(
            @PathVariable String instrumentCode,
            @Valid @RequestBody ReplaceInstrumentSourcePlanRequest request
    ) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        replaceInstrumentSourcePlanService.replace(toPlan(instrumentCode, request));
        return ResponseEntity.noContent().build();
    }

    /* Маппинг HTTP-запроса в доменный план. */
    private InstrumentSourcePlan toPlan(String instrumentCode, ReplaceInstrumentSourcePlanRequest request) {
        List<MarketDataSource> sources = request.sources().stream()
                .map(this::toSource)
                .toList();

        return new InstrumentSourcePlan(
                new InstrumentCode(instrumentCode),
                sources
        );
    }

    /* Маппинг HTTP-модели источника в доменный источник. */
    private MarketDataSource toSource(
            ReplaceInstrumentSourcePlanRequest.MarketDataSourceRequest request
    ) {
        return new MarketDataSource(
                new ProviderCode(request.providerCode()),
                request.active(),
                request.priority()
        );
    }
}
