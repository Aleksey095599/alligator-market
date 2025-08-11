-- Создает таблицу currency
CREATE TABLE currency (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    code VARCHAR(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    decimal INTEGER NOT NULL,
    CONSTRAINT uq_currency_code UNIQUE (code),
    CONSTRAINT uq_currency_name UNIQUE (name),
    CONSTRAINT uq_currency_country UNIQUE (country)
);
