-- instrument_feed_config: конфигурация источников котировок для инструмента.
CREATE TABLE instrument_feed_config
(
    -- Суррогатный PK
    id                BIGSERIAL PRIMARY KEY,

    -- Связи и бизнес-поля
    instrument_id     BIGINT       NOT NULL,
    provider_code     VARCHAR(50)  NOT NULL,
    feed_role         VARCHAR(16)  NOT NULL,
    enabled           BOOLEAN      NOT NULL,

    -- Аудит/версионирование
    version           BIGINT       NOT NULL DEFAULT 0,
    created_timestamp TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(255) NOT NULL,
    created_via       VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255) NOT NULL,
    updated_via       VARCHAR(255) NOT NULL,

    -- Ограничения ссылочной целостности
    CONSTRAINT fk_ifc_instrument
        FOREIGN KEY (instrument_id) REFERENCES instrument (id),

    -- Ограничения уникальности
    CONSTRAINT uq_ifc_instrument_feed_role UNIQUE (instrument_id, feed_role),
    CONSTRAINT uq_ifc_instrument_provider_code UNIQUE (instrument_id, provider_code),

    -- Ограничения доменной валидности
    CONSTRAINT chk_ifc_provider_code_pattern
        CHECK (provider_code = upper(provider_code) AND provider_code ~ '^[A-Z0-9_.-]+$'),
    CONSTRAINT chk_ifc_provider_code_not_blank
        CHECK (length(btrim(provider_code)) > 0),
    CONSTRAINT chk_ifc_feed_role_allowed
        CHECK (feed_role IN ('PRIMARY', 'SECONDARY')),
    CONSTRAINT chk_ifc_version_non_negative
        CHECK (version >= 0)
);

-- Индекс для массового отбора включённых конфигураций.
CREATE INDEX idx_ifc_enabled ON instrument_feed_config (enabled);
