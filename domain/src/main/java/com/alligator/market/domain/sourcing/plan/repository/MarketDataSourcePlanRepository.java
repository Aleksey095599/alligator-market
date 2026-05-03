package com.alligator.market.domain.sourcing.plan.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.capture.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.sourcing.plan.MarketDataSourcePlan;

import java.util.List;
import java.util.Optional;

/**
 * Порт доступа к планам источников рыночных данных для инструментов.
 */
public interface MarketDataSourcePlanRepository {

    /**
     * Возвращает план источников для заданного инструмента.
     */
    Optional<MarketDataSourcePlan> findByCollectionProcessCodeAndInstrumentCode(
            MarketDataCollectionProcessCode collectionProcessCode,
            InstrumentCode instrumentCode
    );

    /**
     * Возвращает планы источников для всех инструментов.
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
     * Удаляет существующий план инструмента.
     *
     * @return true, если план существовал и был удалён; false, если плана не было
     */
    boolean deleteIfExistsByCollectionProcessCodeAndInstrumentCode(
            MarketDataCollectionProcessCode collectionProcessCode,
            InstrumentCode instrumentCode
    );
}
