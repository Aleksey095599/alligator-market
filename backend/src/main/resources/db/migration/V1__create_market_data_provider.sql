-- Таблица провайдеров рыночных данных.
CREATE TABLE market_data_provider (
    id                BIGSERIAL PRIMARY KEY,
    provider_code     VARCHAR(50)   NOT NULL,
    version           BIGINT        NOT NULL,
    created_timestamp TIMESTAMPTZ   NOT NULL,
    created_by        VARCHAR(255)  NOT NULL,
    created_via       VARCHAR(255)  NOT NULL,
    updated_timestamp TIMESTAMPTZ   NOT NULL,
    updated_by        VARCHAR(255)  NOT NULL,
    updated_via       VARCHAR(255)  NOT NULL,
    CONSTRAINT uq_provider_code UNIQUE (provider_code)
);
