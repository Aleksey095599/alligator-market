-- Инициализация схемы и таблиц проекта.

-- Создаём схему при отсутствии.
CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public;

-- =============================
-- Таблица валют
-- =============================
CREATE TABLE IF NOT EXISTS currency (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    decimal_digits INTEGER NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL
);

-- Уникальные ограничения валют
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_currency_code') THEN
        ALTER TABLE currency ADD CONSTRAINT uq_currency_code UNIQUE (code);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_currency_name') THEN
        ALTER TABLE currency ADD CONSTRAINT uq_currency_name UNIQUE (name);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_currency_country') THEN
        ALTER TABLE currency ADD CONSTRAINT uq_currency_country UNIQUE (country);
    END IF;
END $$;

-- =============================
-- Таблица валютных пар
-- =============================
CREATE TABLE IF NOT EXISTS currency_pair (
    id BIGSERIAL PRIMARY KEY,
    base VARCHAR(3) NOT NULL,
    quote VARCHAR(3) NOT NULL,
    pair_code VARCHAR(6) NOT NULL,
    decimal INTEGER NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL
);

-- Уникальные и внешние ограничения валютных пар
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_pair_code') THEN
        ALTER TABLE currency_pair ADD CONSTRAINT uq_pair_code UNIQUE (pair_code);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_pair_base') THEN
        ALTER TABLE currency_pair
            ADD CONSTRAINT fk_pair_base FOREIGN KEY (base) REFERENCES currency(code);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_pair_quote') THEN
        ALTER TABLE currency_pair
            ADD CONSTRAINT fk_pair_quote FOREIGN KEY (quote) REFERENCES currency(code);
    END IF;
END $$;

-- =============================
-- Таблица FX OUTRIGHT инструментов
-- =============================
CREATE TABLE IF NOT EXISTS fx_outright_instruments (
    internal_code VARCHAR(12) PRIMARY KEY,
    pair_code VARCHAR(6) NOT NULL,
    value_date_code VARCHAR(4) NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL
);

-- Связь FX OUTRIGHT инструментов с валютной парой
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_fx_outright_pair') THEN
        ALTER TABLE fx_outright_instruments
            ADD CONSTRAINT fk_fx_outright_pair FOREIGN KEY (pair_code) REFERENCES currency_pair(pair_code);
    END IF;
END $$;

-- =============================
-- Таблица профилей провайдеров
-- =============================
CREATE TABLE IF NOT EXISTS provider_profile (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(10) NOT NULL,
    provider_code VARCHAR(50) NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    delivery_mode VARCHAR(10) NOT NULL,
    access_method VARCHAR(20) NOT NULL,
    supports_bulk_subscription BOOLEAN NOT NULL,
    min_poll_period_ms INTEGER NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL
);

-- =============================
-- Таблица типов инструментов провайдера
-- =============================
CREATE TABLE IF NOT EXISTS provider_profile_instrument_type (
    provider_id BIGINT NOT NULL,
    instrument_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (provider_id, instrument_type)
);

-- Связь типов инструментов с профилем провайдера
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_provider_profile_instrument_type_provider_profile') THEN
        ALTER TABLE provider_profile_instrument_type
            ADD CONSTRAINT fk_provider_profile_instrument_type_provider_profile FOREIGN KEY (provider_id) REFERENCES provider_profile(id);
    END IF;
END $$;
