-- instrument_market_data_source: источники рыночных данных для конкретного инструмента.
CREATE TABLE instrument_market_data_source
(
    -- Суррогатный PK
    id              BIGSERIAL   PRIMARY KEY,

    -- Идентичность источника рыночных данных
    instrument_code VARCHAR(50) NOT NULL,
    provider_code   VARCHAR(50) NOT NULL,
    active          BOOLEAN     NOT NULL,
    priority        INTEGER     NOT NULL,

    -- Ограничения ссылочной целостности
    CONSTRAINT fk_instr_market_data_source_instrument FOREIGN KEY (instrument_code) REFERENCES instrument_base (code),

    -- Ограничения уникальности
    CONSTRAINT uk_instr_market_data_source_instr_provider UNIQUE (instrument_code, provider_code),
    CONSTRAINT uk_instr_market_data_source_instr_priority UNIQUE (instrument_code, priority),

    -- Ограничения доменной валидности
    CONSTRAINT chk_instr_market_data_source_priority CHECK (priority >= 0)
);
