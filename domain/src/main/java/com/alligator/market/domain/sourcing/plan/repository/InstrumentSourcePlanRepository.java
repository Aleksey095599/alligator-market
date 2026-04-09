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
     * Создаёт root-plan, если он ещё не существует.
     *
     * @return true, если root-plan создан; false, если план уже существует
     */
    boolean createIfAbsent(InstrumentSourcePlan plan);


    /**
     * Условно заменяет содержимое существующего root-plan.
     *
     * @return true, если root-plan существовал и был обновлён; false, если план отсутствует
     */
    boolean replaceIfExists(InstrumentSourcePlan plan);

    /**
     * Условно удаляет существующий root-plan инструмента.
     *
     * @return true, если root-plan существовал и был удалён; false, если плана не было
     */
    boolean deleteIfExistsByInstrumentCode(InstrumentCode instrumentCode);
}
