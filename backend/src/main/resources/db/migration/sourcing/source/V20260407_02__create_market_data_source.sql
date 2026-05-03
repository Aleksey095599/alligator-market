-- market_data_source: источники рыночных данных для конкретного инструмента
CREATE TABLE market_data_source
(
    -- Суррогатный PK
    id              BIGSERIAL   PRIMARY KEY,

    -- Идентичность источника рыночных данных
    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code          VARCHAR(50)  NOT NULL,
    provider_code            VARCHAR(50)  NOT NULL,
    active                   BOOLEAN      NOT NULL,
    priority                 INTEGER      NOT NULL,

    -- Ограничения ссылочной целостности
    CONSTRAINT fk_market_data_source_plan
        FOREIGN KEY (collection_process_code, instrument_code)
            REFERENCES source_plan (collection_process_code, instrument_code)
            ON DELETE CASCADE,

    -- Ограничения уникальности
    CONSTRAINT uq_market_data_source_process_instr_provider UNIQUE (collection_process_code, instrument_code, provider_code),
    CONSTRAINT uq_market_data_source_process_instr_priority UNIQUE (collection_process_code, instrument_code, priority),

    -- Ограничения доменной валидности
    CONSTRAINT chk_market_data_source_priority CHECK (priority >= 0)
);
