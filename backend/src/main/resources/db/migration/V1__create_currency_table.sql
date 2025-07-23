-- Создает таблицу currency
CREATE TABLE currency (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT,
    created_timestamp TIMESTAMP,
    created_by VARCHAR(255),
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    code VARCHAR(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    decimal INTEGER NOT NULL,
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name),
    CONSTRAINT uq_currency_country UNIQUE (country)
);
