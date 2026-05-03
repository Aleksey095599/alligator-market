package com.alligator.market.backend.sourcing.plan.application.query.list;

import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;

import java.util.List;
import java.util.Objects;

/**
 * Сервис чтения всех планов источников рыночных данных.
 */
public final class MarketDataSourcePlanListService {

    /* Репозиторий планов источников. */
    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    public MarketDataSourcePlanListService(MarketDataSourcePlanRepository marketDataSourcePlanRepository) {
        this.marketDataSourcePlanRepository = Objects.requireNonNull(
                marketDataSourcePlanRepository,
                "marketDataSourcePlanRepository must not be null"
        );
    }

    /**
     * Возвращает планы источников для всех инструментов.
     */
    public List<MarketDataSourcePlan> list() {
        return marketDataSourcePlanRepository.findAll();
    }
}
