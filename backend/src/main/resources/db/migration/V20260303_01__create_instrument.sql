-- instrument: базовая таблица финансовых инструментов (JOINED inheritance root).
CREATE TABLE instrument
(
    -- Суррогатный PK
    id                BIGSERIAL PRIMARY KEY,

    -- Идентичность инструмента
    code              VARCHAR(50)  NOT NULL,
    symbol            VARCHAR(50)  NOT NULL,
    type              VARCHAR(32)  NOT NULL,

    -- Аудит/версионирование
    version           BIGINT       NOT NULL DEFAULT 0,
    created_timestamp TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(255) NOT NULL,
    created_via       VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255) NOT NULL,
    updated_via       VARCHAR(255) NOT NULL,

    -- Ограничения уникальности
    CONSTRAINT uq_instrument_code UNIQUE (code),

    -- Ограничения доменной валидности
    CONSTRAINT chk_instrument_code_pattern
        CHECK (code ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_instrument_symbol_pattern
        CHECK (symbol ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_instrument_type_allowed
        CHECK (type IN ('FOREX_SPOT', 'FOREX_FORWARD', 'FOREX_SWAP')),
    CONSTRAINT chk_instrument_version_non_negative
        CHECK (version >= 0)
);

-- Индекс для быстрого отбора по типу инструмента.
CREATE INDEX idx_instrument_type ON instrument (type);
