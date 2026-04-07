-- instrument_base: базовая таблица финансовых инструментов (JOINED inheritance root).
CREATE TABLE instrument_base
(
    -- Суррогатный PK
    id                BIGSERIAL PRIMARY KEY,

    -- Идентичность инструмента
    code              VARCHAR(50)  NOT NULL,
    symbol            VARCHAR(50)  NOT NULL,
    asset_class       VARCHAR(32)  NOT NULL,
    contract_type     VARCHAR(32)  NOT NULL,

    -- Версионирование
    version           BIGINT       NOT NULL DEFAULT 0,

    -- Ограничения уникальности
    CONSTRAINT uq_instrument_code UNIQUE (code),

    -- Ограничения доменной валидности
    CONSTRAINT chk_instrument_code_pattern
        CHECK (code ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_instrument_symbol_pattern
        CHECK (symbol ~ '^[A-Z0-9_]+$'),
    CONSTRAINT chk_instrument_version_non_negative
        CHECK (version >= 0)
);

-- Индексы для быстрого отбора по доменным признакам инструмента.
CREATE INDEX idx_instrument_asset_class ON instrument_base (asset_class);
CREATE INDEX idx_instrument_contract_type ON instrument_base (contract_type);
