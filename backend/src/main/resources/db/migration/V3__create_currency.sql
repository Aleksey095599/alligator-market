-- Миграция создания таблицы справочника валют
CREATE TABLE public.currency (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    decimal_value INTEGER NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL,
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name)
);
