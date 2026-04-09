package com.alligator.market.domain.sourcing.plan.repository;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;

import java.util.List;
import java.util.Optional;

/**
 * Порт доступа к планам источников рыночных данных для инструментов.
 */
public interface InstrumentSourcePlanRepository {

    /**
     * Возвращает план источников для заданного инструмента.
     */
    Optional<InstrumentSourcePlan> findByInstrumentCode(InstrumentCode instrumentCode);

    /**
     * Возвращает планы источников для всех инструментов.
     */
    List<InstrumentSourcePlan> findAll();

    /**
     * Создаёт план источников, если план не существует.
     */
    boolean createIfAbsent(InstrumentSourcePlan plan);


    /**
     * Заменяет существующий план источников на новый.
     */
    void replace(InstrumentSourcePlan plan);

    /**
     * Удаляет план источников для заданного инструмента.
     */
    boolean deleteByInstrumentCode(InstrumentCode instrumentCode);
}
