package com.alligator.market.backend.sourceplan.plan.application.command.delete;

import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.repository.MarketDataSourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис удаления плана источников рыночных данных для инструмента.
 */
@Slf4j
public final class DeleteMarketDataSourcePlanService {

    /* Репозиторий планов источников. */
    private final MarketDataSourcePlanRepository marketDataSourcePlanRepository;

    public DeleteMarketDataSourcePlanService(
            MarketDataSourcePlanRepository marketDataSourcePlanRepository
    ) {
        this.marketDataSourcePlanRepository = Objects.requireNonNull(
                marketDataSourcePlanRepository,
                "marketDataSourcePlanRepository must not be null"
        );
    }

    /**
     * Удаляет существующий план источников для инструмента.
     */
    public void delete(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Условно удаляем root-plan и сигнализируем, если его не было
        boolean deleted = marketDataSourcePlanRepository
                .deleteIfExistsByMarketDataCapturerCodeAndInstrumentCode(capturerCode, instrumentCode);
        if (!deleted) {
            log.warn(
                    "Market data source plan was not found and was not deleted: capturerCode={}, instrumentCode={}",
                    capturerCode.value(),
                    instrumentCode.value()
            );
            throw new MarketDataSourcePlanNotFoundException(capturerCode, instrumentCode);
        }

        log.info(
                "Market data source plan deleted: capturerCode={}, instrumentCode={}",
                capturerCode.value(),
                instrumentCode.value()
        );
    }
}
