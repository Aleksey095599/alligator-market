package com.alligator.market.domain.sourcing.port;

/**
 * Порт доступа планам источников рыночных данных для инструментов.
 */
public interface InstrumentMarketDataSourceRepository {

    /**
     * Возвращает конфигурацию источников для заданного инструмента.
     */
    Optional<InstrumentSourceConfiguration> findByInstrumentCode(InstrumentCode instrumentCode);

    /**
     * Возвращает все конфигурации источников.
     */
    List<InstrumentSourceConfiguration> findAll();

    /**
     * Сохраняет конфигурацию источников инструмента.
     *
     * <p>Если конфигурация для инструмента уже существует,
     * репозиторий должен заменить ее целиком.</p>
     */
    void save(InstrumentSourceConfiguration configuration);
}
