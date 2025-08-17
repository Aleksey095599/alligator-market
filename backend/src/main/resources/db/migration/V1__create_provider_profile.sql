-- Миграция создания таблицы профилей провайдеров рыночных данных
CREATE TABLE public.provider_profile (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(10) NOT NULL,
    provider_code VARCHAR(50) NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    delivery_mode VARCHAR(10) NOT NULL,
    access_method VARCHAR(20) NOT NULL,
    bulk_subscription BOOLEAN NOT NULL,
    min_poll_ms INTEGER NOT NULL,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_via VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    updated_via VARCHAR(255) NOT NULL
);
