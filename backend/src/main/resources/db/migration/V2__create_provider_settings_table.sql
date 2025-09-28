-- Миграция Flyway: создание таблицы настроек провайдеров

CREATE TABLE provider_settings (
    id                          BIGSERIAL PRIMARY KEY,
    version                     BIGINT                  NOT NULL,
    created_timestamp           TIMESTAMPTZ             NOT NULL,
    created_by                  VARCHAR(255)            NOT NULL,
    created_via                 VARCHAR(255)            NOT NULL,
    updated_timestamp           TIMESTAMPTZ             NOT NULL,
    updated_by                  VARCHAR(255)            NOT NULL,
    updated_via                 VARCHAR(255)            NOT NULL,
    provider_code               VARCHAR(50)             NOT NULL,
    min_update_interval_seconds BIGINT                  NOT NULL,
    CONSTRAINT uq_provider_settings_provider_code UNIQUE (provider_code)
);
