package com.alligator.market.backend.sourcing.plan.application.command.delete;

import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import com.alligator.market.domain.sourcing.plan.repository.MarketDataSourcePlanRepository;
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
            MDCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        // Условно удаляем root-plan и сигнализируем, если его не было
        boolean deleted = marketDataSourcePlanRepository
                .deleteIfExistsByMDCaptureProcessCodeAndInstrumentCode(captureProcessCode, instrumentCode);
        if (!deleted) {
            log.warn(
                    "Market data source plan was not found and was not deleted: captureProcessCode={}, instrumentCode={}",
                    captureProcessCode.value(),
                    instrumentCode.value()
            );
            throw new MarketDataSourcePlanNotFoundException(captureProcessCode, instrumentCode);
        }

        log.info(
                "Market data source plan deleted: captureProcessCode={}, instrumentCode={}",
                captureProcessCode.value(),
                instrumentCode.value()
        );
    }
}
