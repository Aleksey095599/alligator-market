package com.alligator.market.backend.sourcing.plan.application.command.replace;

import com.alligator.market.backend.sourcing.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис полной замены плана источников рыночных данных для инструмента.
 */
@Slf4j
public final class ReplaceMarketDataSourcePlanService {

    /* Репозиторий планов источников. */
    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    /* Валидатор существования процесса фиксации, инструмента и провайдеров из плана. */
    private final MarketDataSourcePlanValidator existenceValidator;

    public ReplaceMarketDataSourcePlanService(
            MarketDataSourcePlanRepository marketDataSourcePlanRepository,
            MarketDataSourcePlanValidator existenceValidator
    ) {
        this.marketDataSourcePlanRepository = Objects.requireNonNull(
                marketDataSourcePlanRepository,
                "marketDataSourcePlanRepository must not be null"
        );
        this.existenceValidator = Objects.requireNonNull(
                existenceValidator,
                "existenceValidator must not be null"
        );
    }

    /**
     * Полностью заменяет содержимое существующего плана источников.
     */
    public void replace(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        // Проверяем, что процесс фиксации реально существует
        existenceValidator.ensureCaptureProcessExists(plan);

        // Проверяем, что инструмент реально существует
        existenceValidator.ensureInstrumentExists(plan);

        // Проверяем, что все коды провайдеров из плана существуют
        existenceValidator.ensureProvidersExist(plan);

        // Условно заменяем содержимое root-plan и сигнализируем, если плана не было
        boolean replaced = marketDataSourcePlanRepository.replaceIfExists(plan);
        if (!replaced) {
            log.warn(
                    "Market data source plan was not found and was not replaced: captureProcessCode={}, instrumentCode={}",
                    plan.captureProcessCode().value(),
                    plan.instrumentCode().value()
            );
            throw new MarketDataSourcePlanNotFoundException(plan.captureProcessCode(), plan.instrumentCode());
        }

        log.info(
                "Market data source plan replaced: captureProcessCode={}, instrumentCode={}, sourceCount={}",
                plan.captureProcessCode().value(),
                plan.instrumentCode().value(),
                plan.sources().size()
        );
    }
}
