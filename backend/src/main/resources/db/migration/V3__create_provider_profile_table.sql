-- Создает таблицу provider_profile
CREATE TABLE provider_profile (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL,
    provider_code VARCHAR(50) NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    delivery_mode VARCHAR(10) NOT NULL,
    access_method VARCHAR(20) NOT NULL,
    supports_bulk_subscription BOOLEAN NOT NULL,
    min_poll_period_ms INTEGER NOT NULL
);
