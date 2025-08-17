-- Миграция создания таблицы финансовых инструментов
CREATE TABLE public.instrument (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(32) NOT NULL,
    type VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL,
    CONSTRAINT uq_instrument_code UNIQUE (code)
);
