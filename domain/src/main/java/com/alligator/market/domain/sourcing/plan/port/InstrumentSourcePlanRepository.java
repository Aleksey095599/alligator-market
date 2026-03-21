package com.alligator.market.domain.sourcing.plan.port;

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
     * Сохраняет план источников для заданного инструмента.
     *
     * <p>Если план источников заданного инструмента уже существует,
     * репозиторий должен заменить его целиком.</p>
     */
    void save(InstrumentSourcePlan plan);
}
