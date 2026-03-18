-- instrument_market_data_source: источники рыночных данных для конкретного инструмента.
CREATE TABLE instrument_market_data_source
(
    -- Суррогатный PK
    id              BIGSERIAL   PRIMARY KEY,

    -- Натуральные ссылки на инструмент и провайдера
    instrument_code VARCHAR(50) NOT NULL,
    provider_code   VARCHAR(50) NOT NULL,

    -- Управление активностью и порядком выбора источника
    active          BOOLEAN     NOT NULL,
    priority        INTEGER     NOT NULL,

    -- Ограничения ссылочной целостности
    CONSTRAINT fk_instr_market_data_source_instrument
        FOREIGN KEY (instrument_code) REFERENCES instrument_base (code),
    CONSTRAINT fk_instr_market_data_source_provider
        FOREIGN KEY (provider_code) REFERENCES provider_passport (provider_code),

    -- Ограничения уникальности
    CONSTRAINT uk_instr_market_data_source_instr_provider
        UNIQUE (instrument_code, provider_code),
    CONSTRAINT uk_instr_market_data_source_instr_priority
        UNIQUE (instrument_code, priority),

    -- Ограничения доменной валидности
    CONSTRAINT chk_instr_market_data_source_priority
        CHECK (priority >= 0)
);

-- Индекс для ускорения выборок/джойнов по провайдеру.
CREATE INDEX idx_instr_market_data_source_provider
    ON instrument_market_data_source (provider_code);
