-- market_data_source: источники рыночных данных для конкретного инструмента.
CREATE TABLE market_data_source
(
    -- Суррогатный PK
    id              BIGSERIAL   PRIMARY KEY,

    -- Идентичность источника рыночных данных
    instrument_code VARCHAR(50) NOT NULL,
    provider_code   VARCHAR(50) NOT NULL,
    active          BOOLEAN     NOT NULL,
    priority        INTEGER     NOT NULL,

    -- Ограничения ссылочной целостности
    CONSTRAINT fk_market_data_source_plan
        FOREIGN KEY (instrument_code)
            REFERENCES instrument_source_plan (instrument_code)
            ON DELETE CASCADE,

    -- Ограничения уникальности
    CONSTRAINT uk_market_data_source_instr_provider UNIQUE (instrument_code, provider_code),
    CONSTRAINT uk_market_data_source_instr_priority UNIQUE (instrument_code, priority),

    -- Ограничения доменной валидности
    CONSTRAINT chk_market_data_source_priority CHECK (priority >= 0)
);
