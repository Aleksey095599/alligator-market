package com.alligator.market.domain.sourcing.plan.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;

import java.util.List;
import java.util.Optional;

/**
 * Порт доступа к планам источников рыночных данных {@link MarketDataSourcePlan}.
 */
public interface MarketDataSourcePlanRepository {

    /**
     * Возвращает план источников по коду процесса и коду инструмента.
     */
    Optional<MarketDataSourcePlan> findByCaptureProcessCodeAndInstrumentCode(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );

    /**
     * Возвращает все планы источников.
     */
    List<MarketDataSourcePlan> findAll();

    /**
     * Создаёт план, если он ещё не существует.
     *
     * @return true, если план создан; false, если план уже существует
     */
    boolean createIfAbsent(MarketDataSourcePlan plan);


    /**
     * Заменяет содержимое существующего плана.
     *
     * @return true, если план существовал и был обновлён; false, если план отсутствует
     */
    boolean replaceIfExists(MarketDataSourcePlan plan);

    /**
     * Удаляет существующий план инструмента по коду процесса и коду инструмента.
     *
     * @return true, если план существовал и был удалён; false, если плана не было
     */
    boolean deleteIfExistsByCaptureProcessCodeAndInstrumentCode(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    );
}
