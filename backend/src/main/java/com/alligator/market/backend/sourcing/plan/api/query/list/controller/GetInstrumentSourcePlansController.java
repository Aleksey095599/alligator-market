package com.alligator.market.backend.sourcing.plan.api.query.list.controller;

import com.alligator.market.backend.sourcing.plan.api.query.list.dto.InstrumentMarketDataSourceResponse;
import com.alligator.market.backend.sourcing.plan.api.query.list.dto.InstrumentSourcePlanResponse;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import com.alligator.market.domain.sourcing.source.InstrumentMarketDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST-адаптер чтения списка существующих планов источников.
 */
@RestController
@RequestMapping("/api/v1/instrument-source-plans")
public class GetInstrumentSourcePlansController {

    /* Репозиторий планов источников. */
    private final InstrumentSourcePlanRepository instrumentSourcePlanRepository;

    public GetInstrumentSourcePlansController(
            InstrumentSourcePlanRepository instrumentSourcePlanRepository
    ) {
        this.instrumentSourcePlanRepository = Objects.requireNonNull(
                instrumentSourcePlanRepository,
                "instrumentSourcePlanRepository must not be null"
        );
    }

    /**
     * Возвращает список всех существующих планов источников.
     */
    @GetMapping
    public List<InstrumentSourcePlanResponse> getAll() {
        return instrumentSourcePlanRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /* Маппинг доменного плана в DTO ответа. */
    private InstrumentSourcePlanResponse toResponse(InstrumentSourcePlan plan) {
        return new InstrumentSourcePlanResponse(
                plan.instrumentCode().value(),
                plan.sources().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    /* Маппинг доменного источника в DTO ответа. */
    private InstrumentMarketDataSourceResponse toResponse(InstrumentMarketDataSource source) {
        return new InstrumentMarketDataSourceResponse(
                source.providerCode().value(),
                source.active(),
                source.priority()
        );
    }
}
