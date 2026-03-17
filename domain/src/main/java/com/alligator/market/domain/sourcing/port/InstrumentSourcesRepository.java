package com.alligator.market.domain.sourcing.port;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.pool.InstrumentSourcesPool;

import java.util.List;
import java.util.Optional;

/**
 * Порт доступа к пулу источников рыночных данных для инструментов.
 */
public interface InstrumentSourcesRepository {

    /**
     * Возвращает пул источников для заданного инструмента.
     */
    Optional<InstrumentSourcesPool> findByInstrumentCode(InstrumentCode instrumentCode);

    /**
     * Возвращает пулы источников для всех инструментов.
     */
    List<InstrumentSourcesPool> findAll();

    /**
     * Сохраняет пул источников для заданного инструмента.
     *
     * <p>Если пул источников заданного инструмента уже существует,
     * репозиторий должен заменить ее целиком.</p>
     */
    void save(InstrumentSourcesPool configuration);
}
