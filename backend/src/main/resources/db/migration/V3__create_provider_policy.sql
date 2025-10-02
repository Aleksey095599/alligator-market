-- Таблица политик провайдеров.
CREATE TABLE provider_policy (
    id                          BIGINT PRIMARY KEY,
    min_update_interval_seconds BIGINT NOT NULL,
    CONSTRAINT fk_provider_policy_base FOREIGN KEY (id)
        REFERENCES market_data_provider (id)
        ON DELETE CASCADE
);
