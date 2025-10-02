-- Таблица базовых финансовых инструментов.
CREATE TABLE instrument (
    id                BIGSERIAL   PRIMARY KEY,
    code              VARCHAR(32) NOT NULL,
    type              VARCHAR(32) NOT NULL,
    version           BIGINT      NOT NULL,
    created_timestamp TIMESTAMPTZ NOT NULL,
    created_by        VARCHAR(255) NOT NULL,
    created_via       VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMPTZ NOT NULL,
    updated_by        VARCHAR(255) NOT NULL,
    updated_via       VARCHAR(255) NOT NULL,
    CONSTRAINT uq_instrument_code UNIQUE (code)
);
