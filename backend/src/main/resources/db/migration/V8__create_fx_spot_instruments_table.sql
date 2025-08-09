-- Создает таблицу fx_spot_instruments
CREATE TABLE fx_spot_instruments (
    internal_code VARCHAR(12) PRIMARY KEY,
    version BIGINT,
    created_timestamp TIMESTAMP,
    created_by VARCHAR(255),
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    pair_code VARCHAR(6) NOT NULL,
    value_date_code VARCHAR(4) NOT NULL,
    CONSTRAINT fk_fx_spot_pair FOREIGN KEY (pair_code) REFERENCES currency_pair(pair_code)
);
