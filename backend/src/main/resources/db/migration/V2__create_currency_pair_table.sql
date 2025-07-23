-- Создает таблицу currency_pair
CREATE TABLE currency_pair (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT,
    created_timestamp TIMESTAMP,
    created_by VARCHAR(255),
    updated_timestamp TIMESTAMP,
    updated_by VARCHAR(255),
    code1 VARCHAR(3) NOT NULL,
    code2 VARCHAR(3) NOT NULL,
    pair VARCHAR(6) NOT NULL,
    decimal INTEGER NOT NULL,
    CONSTRAINT fk_pair_code1 FOREIGN KEY (code1) REFERENCES currency(code),
    CONSTRAINT fk_pair_code2 FOREIGN KEY (code2) REFERENCES currency(code),
    CONSTRAINT uq_currency_pair UNIQUE (pair)
);
