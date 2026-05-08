package com.alligator.market.backend.sourceplan.plan.application.command.delete;

import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Сервис удаления плана источников рыночных данных для инструмента.
 */
@Slf4j
public final class DeleteSourcePlanService {

    /* Репозиторий планов источников. */
    private final SourcePlanRepository sourcePlanRepository;

    public DeleteSourcePlanService(
            SourcePlanRepository sourcePlanRepository
    ) {
        this.sourcePlanRepository = Objects.requireNonNull(
                sourcePlanRepository,
                "sourcePlanRepository must not be null"
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
        boolean deleted = sourcePlanRepository
                .deleteIfExistsByMarketDataCapturerCodeAndInstrumentCode(capturerCode, instrumentCode);
        if (!deleted) {
            log.warn(
                    "Source plan was not found and was not deleted: capturerCode={}, instrumentCode={}",
                    capturerCode.value(),
                    instrumentCode.value()
            );
            throw new SourcePlanNotFoundException(capturerCode, instrumentCode);
        }

        log.info(
                "Source plan deleted: capturerCode={}, instrumentCode={}",
                capturerCode.value(),
                instrumentCode.value()
        );
    }
}
