package com.alligator.market.backend.sourcing.plan.application.command.create;

import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.command.common.MarketDataSourcePlanValidator;
import com.alligator.market.domain.sourceplan.MarketDataSourcePlan;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис создания плана источников рыночных данных для инструмента.
 *
 * <p>Назначение сервиса — выполнить внешние логические проверки перед созданием плана,
 * а затем делегировать сохранение в доменный репозиторий.</p>
 */
@Slf4j
public final class CreateMarketDataSourcePlanService {

    /* Репозиторий планов источников. */
    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    /* Валидатор существования процесса захвата, инструмента и провайдеров из плана. */
    private final MarketDataSourcePlanValidator existenceValidator;

    public CreateMarketDataSourcePlanService(
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
     * Создаёт новый план источников для инструмента.
     */
    public void create(MarketDataSourcePlan plan) {
        Objects.requireNonNull(plan, "plan must not be null");

        // Проверяем, что процесс захвата реально существует
        existenceValidator.ensureMarketDataCaptureProcessExists(plan);

        // Проверяем, что инструмент реально существует
        existenceValidator.ensureInstrumentExists(plan);

        // Проверяем, что все коды провайдеров из плана существуют
        existenceValidator.ensureProvidersExist(plan);

        // Атомарно создаём план, если он ещё не существует
        if (!marketDataSourcePlanRepository.createIfAbsent(plan)) {
            log.warn(
                    "Market data source plan already exists and was not created: captureProcessCode={}, instrumentCode={}",
                    plan.captureProcessCode().value(),
                    plan.instrumentCode().value()
            );
            throw new MarketDataSourcePlanAlreadyExistsException(plan.captureProcessCode(), plan.instrumentCode());
        }

        log.info(
                "Market data source plan created: captureProcessCode={}, instrumentCode={}, sourceCount={}",
                plan.captureProcessCode().value(),
                plan.instrumentCode().value(),
                plan.entries().size()
        );
    }
}
