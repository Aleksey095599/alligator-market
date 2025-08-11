-- Создает таблицу currency_pair
CREATE TABLE currency_pair (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    code1 VARCHAR(3) NOT NULL,
    code2 VARCHAR(3) NOT NULL,
    pair VARCHAR(6) NOT NULL,
    decimal INTEGER NOT NULL,
    CONSTRAINT fk_pair_code1 FOREIGN KEY (code1) REFERENCES currency(code),
    CONSTRAINT fk_pair_code2 FOREIGN KEY (code2) REFERENCES currency(code),
    CONSTRAINT uq_currency_pair UNIQUE (pair)
);
